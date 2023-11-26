const loginDash = document.querySelector('.login-dash');
const loginFormTitle = document.querySelector('.login-form-title');

loginDash.addEventListener('animationiteration', (event) => {
    if (event.animationName === 'colorChange') {
        loginFormTitle.style.textShadow = '0 0 5px #e4fd06, 0 0 55px #ff6a00, -20px -22px 2px rgba(2, 2, 2, 0)';
    }
});
