function handleDomainChange() {
    const emailDomainSelect = document.getElementById('email-domain');
    const emailDomainInput = document.getElementById('email-domain-input');

    if (emailDomainSelect.value === 'custom') {
        emailDomainInput.removeAttribute('readonly');
        emailDomainInput.value = '';
        emailDomainInput.focus();
    } else {
        emailDomainInput.setAttribute('readonly', true);
        emailDomainInput.value = emailDomainSelect.value;
    }
}

document.getElementById('signup-form').addEventListener('submit', function(event) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const passwordMessage = document.getElementById('password-message');

    if (password !== confirmPassword) {
        event.preventDefault();
        passwordMessage.textContent = '패스워드가 일치하지 않습니다';
        passwordMessage.style.color = 'red';
        return;
    } else {
        passwordMessage.textContent = '';
    }

    const emailUser = document.getElementById('email-user').value;
    const emailDomainInput = document.getElementById('email-domain-input').value;
    const email = emailUser +"@"+ emailDomainInput;
    document.getElementById('email').value = email;
});

document.getElementById('confirm-password').addEventListener('input', function() {
    const password = document.getElementById('password').value;
    const confirmPassword = this.value;
    const passwordMessage = document.getElementById('password-message');

    if (password !== confirmPassword) {
        passwordMessage.textContent = '패스워드가 일치하지 않습니다';
        passwordMessage.style.color = 'red';
    } else {
        passwordMessage.textContent = '';
    }
});