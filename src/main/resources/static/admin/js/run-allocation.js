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
    toast.classList.remove("bg-red-500", "bg-yellow-400");

    // Apply based on type
    if (type === 'error') {
        toast.classList.add("bg-red-500");
    } else if (type === 'warning') {
        toast.classList.add("bg-yellow-400", "text-gray-900");
    }

    toast.classList.remove("hidden");
    toast.classList.add("flex");

    setTimeout(() => {
        hideToast();
    }, 3000);
}

function hideToast() {
    const toast = document.getElementById("toast-error");
    toast.classList.remove("flex");
    toast.classList.add("hidden");
}

document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.semester-btn');
    const hiddenInput = document.getElementById('selectedSemester');
    const form = document.getElementById('allocationForm');

    const pathSegments = window.location.pathname.split('/');
    const lastSegment = pathSegments[pathSegments.length - 1];
    const currentSemester = parseInt(lastSegment);

    // Set initial semester from URL if valid (5 to 8), else default to 5
    let initialSemester = [5, 6, 7, 8].includes(currentSemester) ? currentSemester : 5;
    hiddenInput.value = initialSemester;

    buttons.forEach(btn => {
        if (parseInt(btn.getAttribute('data-sem')) === initialSemester) {
            btn.style.backgroundColor = '#2D9D5D'; // green
        } else {
            btn.style.backgroundColor = '#1E3C72'; // blue
        }
    });

    // Handle click on semester buttons
    buttons.forEach(btn => {
        btn.addEventListener('click', function () {
            const selected = this.getAttribute('data-sem');
            hiddenInput.value = selected;

            // Update button styles
            buttons.forEach(b => {
                b.style.backgroundColor = '#1E3C72';
            });
            this.style.backgroundColor = '#2D9D5D';
        });
    });

    // Set form action on submit
    form.addEventListener('submit', function () {
        const semester = hiddenInput.value;
        this.setAttribute('action', `/execute-allocation/${semester}`);
    });
});
