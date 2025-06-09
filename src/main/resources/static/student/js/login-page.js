// Toast Notification
function showToast(message, type = 'error') {
    const toast = document.getElementById("toast-error");
    const text = document.getElementById("toast-message");

    text.innerText = message;

    // Reset any previous background color
    toast.classList.remove("bg-red-600", "bg-yellow-400");

    // Apply based on type
    if (type === 'error') {
        toast.classList.add("bg-red-600");
    } else if (type === 'warning') {
        toast.classList.add("bg-yellow-400", "text-gray-900");
    }

    toast.classList.remove("hidden");
    toast.classList.add("flex");

    setTimeout(() => {
        hideToast();
    }, 5000);
}

function hideToast() {
    const toast = document.getElementById("toast-error");
    toast.classList.remove("flex");
    toast.classList.add("hidden");
}

if(sessionExpired) {
    printSessionExpiredResponse();
}

function printSessionExpiredResponse() {
    if(sessionExpired.status === 419)
        showToast(sessionExpired.message);
    else
        showToast("Internal Server Error! Please Contact support.")
}

function togglePasswordVisibility() {
    const input = document.getElementById('floating_password');
    const icon = document.getElementById('passwordToggleIcon');

    const isPassword = input.type === 'password';
    input.type = isPassword ? 'text' : 'password';
    icon.src = isPassword ? '/student/images/view.svg' : '/student/images/close-eye.svg';
    icon.alt = isPassword ? 'Hide password' : 'Show password';
}