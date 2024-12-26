document.addEventListener('DOMContentLoaded', function() {
    const signupBtn = document.getElementById('signup-btn');
    const naverLoginBtn = document.getElementById('naver-login');
    const kakaoLoginBtn = document.getElementById('kakao-login');
    const googleLoginBtn = document.getElementById('google-login');

    signupBtn.addEventListener('click', function() {
        window.location.href = '/signup';
    });

    naverLoginBtn.addEventListener('click', function() {
        window.location.href ='/api/oauth/naver';
    });

    kakaoLoginBtn.addEventListener('click', function() {
        window.location.href ='/api/oauth/kakao';
    });

    googleLoginBtn.addEventListener('click', function() {
        window.location.href ='/api/oauth/google';
    });

    const loginForm = document.getElementById('login-form');

    loginForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email, password: password })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                localStorage.setItem('jwt', data.token);
                window.location.href = '/home';
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });

    // URL에서 토큰을 추출하고 저장한 후 리디렉션
    const urlParams =
        new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    if (token) {
        localStorage.setItem('jwt', token);
        window.location.href = '/home';
    }
});
