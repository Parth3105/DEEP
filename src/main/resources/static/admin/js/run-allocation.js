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
    }, 5000);
}

function hideToast() {
    const toast = document.getElementById("toast-error");
    toast.classList.remove("flex");
    toast.classList.add("hidden");
}

function updateAllocationSummary(sem) {
    const status = allocationStatusMap?.[sem] || {};
    const allocated = allocatedCountMap?.[sem] ?? 0;
    const unallocated = unallocatedCountMap?.[sem] ?? 0;

    const statusDiv = document.getElementById("allocation-status");
    const statusText = document.getElementById("allocation-status-text");
    const allocatedDiv = document.getElementById("allocated-count");
    const unallocatedDiv = document.getElementById("unallocated-count");

    if (!status || Object.keys(status).length === 0 || status.status === 204) {
        statusDiv.className = "bg-yellow-400 text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Yet to run";
    } else if (status.status === 200) {
        statusDiv.className = "bg-gradient-to-r from-[#27AE60] to-[#2ECC71] text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = status.message;
    } else if (status.status === 500) {
        statusDiv.className = "bg-red-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Failed";
        showToast(status.message);
    } else {
        statusDiv.className = "bg-gray-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Unknown";
    }

    allocatedDiv.textContent = allocated;
    unallocatedDiv.textContent = unallocated;
}

document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.semester-btn');
    const hiddenInput = document.getElementById('selectedSemester');
    const form = document.getElementById('allocationForm');

    let initialSemester = [5, 6, 7, 8].includes(selectedSemester) ? selectedSemester : 5;

    selectedSemester = initialSemester;
    hiddenInput.value = initialSemester;

    // Style buttons
    buttons.forEach(btn => {
        const sem = parseInt(btn.getAttribute('data-sem'));
        btn.style.backgroundColor = sem === initialSemester ? '#2D9D5D' : '#1E3C72';

        btn.addEventListener('click', () => {
            const selected = parseInt(btn.getAttribute('data-sem'));
            selectedSemester = selected;
            hiddenInput.value = selected;

            buttons.forEach(b => b.style.backgroundColor = '#1E3C72');
            btn.style.backgroundColor = '#2D9D5D';

            updateAllocationSummary(selected);
        });
    });

    form.addEventListener('submit', function () {
        const semester = hiddenInput.value;
        this.setAttribute('action', `/admin/execute-allocation/${semester}`);
    });

    // Initial summary
    updateAllocationSummary(initialSemester);
});
