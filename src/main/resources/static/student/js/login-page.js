document.addEventListener('DOMContentLoaded', function () {
    // Check for login error in URL
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error')) {
        showToast('Invalid username or password.', statusColors.ERROR);
    }

    // Session expired response
    if (sessionExpired) {
        printStatusResponse(sessionExpired);
    }

    // Reset password response
    if (resetResponse) {
        printStatusResponse(resetResponse);
    }
});

function togglePasswordVisibility() {
    const input = document.getElementById('floating_password');
    const icon = document.getElementById('passwordToggleIcon');

    const isPassword = input.type === 'password';
    input.type = isPassword ? 'text' : 'password';
    icon.src = isPassword ? '/student/images/view.svg' : '/student/images/close-eye.svg';
    icon.alt = isPassword ? 'Hide password' : 'Show password';
}