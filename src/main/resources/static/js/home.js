document.addEventListener('DOMContentLoaded', function () {
    const sidebar = document.querySelector('.sidebar');
    const roomButton = document.getElementById('room-button');
    const searchButton = document.getElementById('search-button');
    const invitationButton = document.getElementById('invitation-button');
    let stompClient;
    let currentSubscription = null;
    let isComposing = false;
    const messageInput = document.getElementById('message');

    // IME 조합으로 인하여 엔터를 누를 시 한글은 메시지를 두번 보내게되는 문제가 생김
    messageInput.addEventListener('compositionstart', function () {
        isComposing = true;
        console.log(`isComposing true ? : ${isComposing}`)
    });

    // 조합 끝날 때
    messageInput.addEventListener('compositionend', function () {
        isComposing = false;
        console.log(`isComposing false ? : ${isComposing}`)
    });

    connectSocket();

    function subscribeToRoom(roomKey) {
        if (currentSubscription) {
            currentSubscription.unsubscribe();
            currentSubscription = null;
        }

        if (stompClient && stompClient.connected) {
            currentSubscription = stompClient.subscribe(`/topic/messages/${roomKey}`, function(messageOutput) {
                try {
                    const message = JSON.parse(messageOutput.body);
                    showMessage(message);
                } catch (e) {
                    console.error('Invalid JSON received:', messageOutput.body);
                }
            });
        } else {
            console.error('STOMP client is not connected.');
        }
    }

    function connectSocket() {
        if (stompClient && stompClient.connected) {
            return; // 이미 연결되어 있다면 재연결하지 않음
        }
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        //로그내용을 안보여주기위한 내용
        stompClient.debug = function() {};
        let token = localStorage.getItem('jwt');

        stompClient.connect({ 'Authorization': `Bearer ` + token }, function(frame) {
            // console.log('Connected: ' + frame);
        }, function(error) {
            console.error('STOMP error:', error);
        });
    }



    function getCurrentChatingName() {
        let chatingNameElement = document.querySelector('#chat-room-list li.current-room');
        return chatingNameElement ? chatingNameElement.getAttribute('data-chating-name') : null;
    }

    function getCurrentRoomKey() {
        let currentRoomElement = document.querySelector('#chat-room-list li.current-room');
        return currentRoomElement ? currentRoomElement.getAttribute('data-room-key') : null;
    }

    // 버튼 클릭 이벤트 리스너 추가
    roomButton.addEventListener('click', function () {
        sendGETRequest('/api/myrooms').then(data => {
            if (data) {
                displayRooms(data);
            }
        });
    });

    searchButton.addEventListener('click', function () {
        displaySearchForm();
    });

    invitationButton.addEventListener('click', function () {
        sendGETRequest('/api/invitations').then(data => {
            if (data) {
                displayInvitations(data);
            }
        });
    });

    const authButton = document.getElementById('auth-button');
    const authText = document.getElementById('auth-text');
    const authIcon = authButton.querySelector('i');

    function updateAuthButton() {
        const jwtToken = localStorage.getItem('jwt');

        if (jwtToken) {
            // 로그아웃 버튼 설정
            authText.textContent = '로그아웃';
            authIcon.className = 'fas fa-sign-out-alt';
            authButton.onclick = logout;
        } else {
            // 로그인 버튼 설정
            authText.textContent = '로그인';
            authIcon.className = 'fas fa-sign-in-alt';
            authButton.onclick = login;
        }
    }

    function login() {
        // 로그인 페이지로 리디렉션
        window.location.href = '/login';
    }

    function logout() {
        localStorage.removeItem('jwt');
        alert('로그아웃되었습니다.');

        // 로그인 페이지로 리디렉션
        window.location.href = '/login';
    }

    updateAuthButton();

    function sendGETRequest(url) {
        let token = localStorage.getItem('jwt');
        return fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .catch(error => {
                console.error('오류 발생:', error);
            });
    }

    // 방 목록을 사이드바에 출력하는 함수
    function displayRooms(rooms) {
        sidebar.innerHTML = '';

        const roomsDiv = document.createElement('div');
        roomsDiv.className = 'rooms';

        const title = document.createElement('h2');
        title.textContent = '채팅방';

        const addRoomBtn = document.createElement('button');
        addRoomBtn.id = 'add-room-btn';
        addRoomBtn.textContent = '+';

        const deleteRoomBtn = document.createElement('button');
        deleteRoomBtn.id = 'delete-room-btn';
        deleteRoomBtn.textContent = '-';

        const ul = document.createElement('ul');
        ul.id = 'chat-room-list';

        rooms.forEach(room => {
            const li = document.createElement('li');
            li.setAttribute('data-email', room.memberEmail);
            li.setAttribute('data-room-key', room.roomKey);
            li.setAttribute('data-chating-name', room.chatingName);
            li.textContent = room.chatingName;
            ul.appendChild(li);
        });

        roomsDiv.appendChild(title);
        roomsDiv.appendChild(addRoomBtn);
        roomsDiv.appendChild(deleteRoomBtn);
        roomsDiv.appendChild(ul);

        sidebar.appendChild(roomsDiv);

        ul.querySelectorAll('li').forEach(item => {
            item.addEventListener('click', function () {
                let previousSelected = ul.querySelector('li.current-room');
                if (previousSelected) {
                    previousSelected.classList.remove('current-room');
                }
                item.classList.add('current-room');
                let roomKey = item.getAttribute('data-room-key')

                loadMessages(roomKey);
                subscribeToRoom(roomKey); // 새로운 방에 구독
            });
        });

        addRoomBtn.addEventListener('click', function () {
            openAddRoomWindow();
        });

        deleteRoomBtn.addEventListener('click', function () {
            deleteCurrentRoom();
        });
    }

    // 검색 폼과 사용자 카드 템플릿을 사이드바에 출력하는 함수
    function displaySearchForm() {
        sidebar.innerHTML = '';

        const form = document.createElement('form');
        form.className = 'search-bar';

        const label = document.createElement('label');
        label.htmlFor = 'search';
        label.className = 'a11yHidden';
        form.appendChild(label);

        const input = document.createElement('input');
        input.type = 'search';
        input.id = 'search';
        input.placeholder = '채팅방 초대할 유저 이메일 검색';
        input.dataset.search = true;
        form.appendChild(input);

        const userCardsDiv = document.createElement('div');
        userCardsDiv.className = 'user-cards';
        userCardsDiv.dataset.memberCardsContainer = true;

        const template = document.createElement('template');
        template.dataset.memberTemplate = true;
        template.innerHTML = `
            <div class="card">
                <div class="header" data-header></div>
                <div class="body" data-body></div>
            </div>
        `;

        sidebar.appendChild(form);
        sidebar.appendChild(userCardsDiv);
        sidebar.appendChild(template);

        input.addEventListener('input', searchMembers);
    }

    function searchMembers(event) {
        const searchInput = event.target;
        const query = searchInput.value.trim();
        const memberCardTemplate = document.querySelector('[data-member-template]');
        const memberCardContainer = document.querySelector('[data-member-cards-container]');
        // const memberEmail = document.getElementById('member-email');
        const token = localStorage.getItem('jwt');

        if (query === "") {
            memberCardContainer.innerHTML = "";
            return;
        }

        fetch('/api/search-member', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ` + token
            },
            body: JSON.stringify({ email: query })
        })
            .then(res => res.json())
            .then(data => {
                memberCardContainer.innerHTML = "";
                data.forEach(member => {
                    const card = memberCardTemplate.content.cloneNode(true).children[0];
                    const header = card.querySelector('[data-header]');
                    const body = card.querySelector('[data-body]');
                    header.textContent = member.name;
                    body.textContent = member.email;

                    card.addEventListener('click', () => openInviteWindow(member.email));

                    memberCardContainer.append(card);
                });
            })
            .catch(err => console.error(err));
    }

    function openInviteWindow(email) {
        // const memberEmail = document.getElementById('member-email').value;
        const screenWidth = window.innerWidth;
        const screenHeight = window.innerHeight;
        const width = 500;
        const height = 300;
        const left = (screenWidth / 2) - (width / 2) + window.screenX;
        const top = (screenHeight / 2) - (height / 2) + window.screenY;

        const newWindow = window.open("", "inviteWindow", `width=${width},height=${height},top=${top},left=${left}`);

        newWindow.document.write(`
            <html>
                <head>
                    <title>채팅방 초대</title>
                    <link rel="stylesheet" href="/css/home.css">
                </head>
                <body>
                    <div class="popup-container">
                        <h1>채팅방을 선택하세요</h1>
                        <ul id="room-list" class="room-list"></ul>
                    </div>
                    <script>
                        const token = localStorage.getItem('jwt');
                        const roomList = document.getElementById('room-list');
                        sendGETRequest('/api/myrooms', token).then(rooms => {
                            if(rooms) {
                                rooms.forEach(room => {
                                    const li = document.createElement('li');
                                    li.className = 'room-item';
                                    li.textContent = room.chatingName;
                                    li.addEventListener('click', () => inviteMember('${email}', room.roomKey, room.chatingName, token));
                                    roomList.appendChild(li);
                                });
                            }
                        })
                        function inviteMember(toMemberEmail, chatingRoomKey, chatingRoomName, token) {
                            fetch('/api/invite-member', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': 'Bearer ' + token
                                },
                                body: JSON.stringify({ toMemberEmail, chatingRoomKey, chatingRoomName})
                            })
                            .then(response => {
                                if (response.ok) {
                                    alert('초대 메시지가 전송되었습니다.');
                                    window.close();
                                } else {
                                    alert('초대 메시지 전송에 실패했습니다.');
                                }
                            })
                            .catch(error => console.error('Error inviting member:', error));
                        }

                        function sendGETRequest(url, token) {
                            return fetch(url, {
                                    method: 'GET',
                                    headers: {
                                        'Content-Type': 'application/json',
                                        'Authorization': 'Bearer ' + token
                                    }
                                })
                                .then(response => response.json())
                                .catch(error => {
                                    console.error('오류 발생:', error);
                                });
                        }
                    </script>
                </body>
            </html>
        `);
    }

    function displayInvitations(invitations) {
        sidebar.innerHTML = '';

        const invitationSection = document.createElement('div');
        invitationSection.className = 'invitation-section';

        invitationSection.innerHTML = `
            <h2>초대 정보</h2>
            <div id="invitation-list">
                ${invitations.map(item => `
                    <li data-room-name="${item.chatingRoomName}" data-room-key="${item.chatingRoomKey}">
                        ${item.chatingRoomName} 방으로 초대장이 왔습니다.
                    </li>
                `).join('')}
            </div>
        `;

        sidebar.appendChild(invitationSection);

        const invitationItems = document.querySelectorAll('#invitation-list li');
        invitationItems.forEach(item => {
            item.addEventListener('click', function () {
                // const toMemberEmail = document.getElementById('member-email').value;
                const roomName = item.getAttribute('data-room-name');
                const roomKey = item.getAttribute('data-room-key');
                let token = localStorage.getItem('jwt');

                if (confirm(`${roomName} 방에 초대되었습니다. 입장하시겠습니까?`)) {
                    fetch('/api/join-chatingroom', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + token
                        },
                        body: JSON.stringify({ chatingName: roomName, roomKey: roomKey })
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('채팅방에 입장하였습니다.');
                            } else {
                                alert('채팅방 입장에 실패했습니다.');
                            }
                            window.location.href = "/home"
                        })
                        .catch(error => console.error('Error entering chat room:', error));
                } else {
                    fetch('/api/delete-invitation', {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + token
                        },
                        body: JSON.stringify({ chatingName: roomName, roomKey: roomKey })
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('초대장이 삭제되었습니다.');
                            } else {
                                alert('초대장 삭제에 실패했습니다.');
                            }
                            window.location.href = "/home"
                        })
                        .catch(error => console.error('Error deleting invitation:', error));
                }
            });
        });
    }



    document.getElementById('send-btn').addEventListener('click', sendMessage);

    document.getElementById('message').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

    function sendMessage() {
        let chatingName = getCurrentChatingName();
        let messageContent = document.getElementById('message').value.trim();
        let roomKey = getCurrentRoomKey();
        let token = localStorage.getItem('jwt');

        if (messageContent && stompClient && !isComposing) {
            let chatMessage = {
                chatingRoomName: chatingName,
                chatingRoomKey: roomKey,
                message: messageContent,
                accessToken: token

            };
            document.getElementById('message').value = '';
            // console.log('Sending message:', chatMessage); // 디버깅용 로그
            stompClient.send("/app/send-message",{ 'Authorization': `Bearer `+ token }, JSON.stringify(chatMessage));
        }

    }

    function showMessage(message) {
        const date = new Date(message.createAt);
        const formattedDateTime = date.getFullYear() + '-'
            + String(date.getMonth() + 1).padStart(2, 0) + '-'
            + String(date.getDate()).padStart(2, 0) + ' '
            + String(date.getHours()).padStart(2, 0) + ':'
            + String(date.getMinutes()).padStart(2, 0);

        let messageElement = document.createElement('div');
        const ul = document.getElementById('chat-room-list');
        let myEmail = ul.querySelector('li.current-room').getAttribute('data-email');
        messageElement.className = message.memberEmail === myEmail ? 'my-message' : 'their-message';
        messageElement.textContent = `${message.memberName}님 : ${message.message}`;

        let dateElement = document.createElement('div');
        dateElement.className = message.memberEmail === myEmail ? 'my-date' : 'their-date';
        dateElement.textContent = `${formattedDateTime}`;

        let chatBox = document.getElementById('chat-box');
        chatBox.appendChild(messageElement);
        chatBox.appendChild(dateElement);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    function loadMessages(roomKey) {
        let token = localStorage.getItem('jwt');

        fetch('/search-messages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer `+ token
            },
            body: JSON.stringify({ chatingRoomKey: roomKey })
        })
            .then(response => response.json())
            .then(messages => {
                let chatBox = document.getElementById('chat-box');
                chatBox.innerHTML = '';
                messages.forEach(showMessage);
            })
            .catch(error => console.error('Error loading messages:', error));
    }

    function openAddRoomWindow() {
        const screenWidth = window.innerWidth;
        const screenHeight = window.innerHeight;
        const width = 500;
        const height = 300;
        const left = (screenWidth / 2) - (width / 2) + window.screenX;
        const top = (screenHeight / 2) - (height / 2) + window.screenY;

        const newWindow = window.open("", "newWindow", `width=${width},height=${height},top=${top},left=${left}`);

        newWindow.document.write(`
            <html>
                <head>
                    <title>채팅방 생성</title>
                    <link rel="stylesheet" href="/css/home.css">
                </head>
                <body>
                    <div class="popup-container">
                        <h1>채팅방 이름을 입력하세요</h1>
                        <form id="new-room-form">
                            <input type="text" id="chating-name" name="chatingName" placeholder="채팅방 이름" required>
                            <button type="submit">확인</button>
                        </form>
                    </div>
                    <script>
                        document.getElementById('new-room-form').addEventListener('submit', function(event) {
                            event.preventDefault();
                            const chatingName = document.getElementById('chating-name').value;
                            let token = localStorage.getItem('jwt');

                            fetch('/api/add-chatingroom', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': \`Bearer \`+ token
                                },
                                body: JSON.stringify({ chatingName })
                            })
                            .then(response => {
                                let url = "/home";
                                window.opener.location.href = url;
                                window.close();
                                alert('채팅방이 생성 됐습니다.!');
                            })
                            .catch(error => {
                                alert('채팅방 생성에 실패했습니다.');
                                console.error('Error:', error);
                            });
                        });
                    </script>
                </body>
            </html>
        `);
    }

    function deleteCurrentRoom() {
        openDeleteWindow();
    }
    function openDeleteWindow() {
        const screenWidth = window.innerWidth;
        const screenHeight = window.innerHeight;
        const width = 500;
        const height = 300;
        const left = (screenWidth / 2) - (width / 2) + window.screenX;
        const top = (screenHeight / 2) - (height / 2) + window.screenY;

        const newWindow = window.open("", "inviteWindow", `width=${width},height=${height},top=${top},left=${left}`);

        newWindow.document.write(`
            <html>
                <head>
                    <title>채팅방 삭제</title>
                    <link rel="stylesheet" href="/css/home.css">
                </head>
                <body>
                    <div class="popup-container">
                        <h1>삭제할 채팅방을 선택해 주세요.</h1>
                        <ul id="room-list" class="room-list"></ul>
                    </div>
                    <script>
                        const token = localStorage.getItem('jwt');
                        const roomList = document.getElementById('room-list');
                        sendGETRequest('/api/myrooms', token).then(rooms => {
                            if(rooms) {
                                rooms.forEach(room => {
                                    const li = document.createElement('li');
                                    li.className = 'room-item';
                                    li.textContent = room.chatingName;
                                    li.addEventListener('click', () => deleteChatingRoom( room.roomKey, room.chatingName, token));
                                    roomList.appendChild(li);
                                });
                            }
                        })
                        function deleteChatingRoom( roomKey, chatingName, token) {
                            
                            if (confirm(chatingName + '방을 정말로 삭제하시겠습니까?')) {
                                fetch('/api/delete-chatingroom', {
                                    method: 'Delete',
                                    headers: {
                                        'Content-Type': 'application/json',
                                        'Authorization': 'Bearer ' + token
                                    },
                                    body: JSON.stringify({ chatingName, roomKey })
                                })
                                .then(response => {
                                    if (response.ok) {
                                        alert('채팅방이 삭제 되었습니다.');
                                        window.close();
                                    } else {
                                        alert('채팅방 삭제가 실패했습니다.');
                                    }
                                })
                                .catch(error => console.error('Error chating-room delete :', error));
                            }
                        }

                        function sendGETRequest(url, token) {
                            return fetch(url, {
                                    method: 'GET',
                                    headers: {
                                        'Content-Type': 'application/json',
                                        'Authorization': 'Bearer ' + token
                                    }
                                })
                                .then(response => response.json())
                                .catch(error => {
                                    console.error('오류 발생:', error);
                                });
                        }
                    </script>
                </body>
            </html>
        `);
    }
});
