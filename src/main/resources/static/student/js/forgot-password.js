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

if(submitResponse) {
    printSubmitResponseResponse();
}

function printSubmitResponseResponse() {
    if(submitResponse.status === 404)
        showToast(submitResponse.message);
    else
        showToast("Internal Server Error! Please Contact support.")
}

document.querySelector("form").addEventListener("submit", function () {
    // Show spinner
    document.getElementById("spinner").classList.remove("hidden");

    // Disable button
    const button = document.getElementById("sendOtpButton");
    button.querySelector("span").textContent = "Sending OTP Request";
    button.disabled = true;
    button.classList.remove("cursor-pointer", "hover:bg-blue-700");
    button.classList.add("opacity-50", "cursor-not-allowed");
});