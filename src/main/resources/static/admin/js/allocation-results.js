// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
  menu.classList.toggle('hidden');
});

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

if(renderResponse) {
    printRenderResponse();
}

function printRenderResponse() {
    if(renderResponse.status === 404)
        showToast(renderResponse.message);
    else
        showToast("Internal Server Error! Please Contact support.")
}

function submitWithPath(event) {
    event.preventDefault();
    const sid = document.getElementById("studentId").value.trim();
    if (sid) {
        window.location.href = `/allocation-results/${encodeURIComponent(sid)}`;
    }
}
