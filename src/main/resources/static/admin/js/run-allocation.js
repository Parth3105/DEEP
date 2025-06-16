const allocationStatusMap = {};
allocationStatusList?.forEach(entry => {
    allocationStatusMap[entry.semester] = {
        allocated: entry.allocatedCount,
        unallocated: entry.unAllocatedCount,
        allocationstatus: entry.statusCode
    };
});

function updateAllocationSummary(sem) {
    const data = allocationStatusMap?.[sem] || {
        allocated: "--",
        unallocated: "--",
        allocationstatus: "204"
    };

    const statusDiv = document.getElementById("allocation-status");
    const statusText = document.getElementById("allocation-status-text");
    const allocatedDiv = document.getElementById("allocated-count");
    const unallocatedDiv = document.getElementById("unallocated-count");

    const status = data.allocationstatus;

    const showToastForSemester = sessionStorage.getItem(`showToastForSemester_${sem}`) === "true";
    switch (status) {
        case 200:
        case "200":
            statusDiv.className = "bg-gradient-to-r from-[#27AE60] to-[#2ECC71] text-white px-7 py-2 rounded-xl font-medium text-lg";
            statusText.textContent = "Success";
            break;
        case 500:
        case "500":
            statusDiv.className = "bg-red-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
            statusText.textContent = "Failed";
            if (showToastForSemester) {
                showToast("Internal server error during allocation.", statusColors.INTERNAL_SERVER_ERROR);
            }
            break;
        case 400:
        case "400":
            statusDiv.className = "bg-red-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
            statusText.textContent = "Failed";
            if (showToastForSemester) {
                showToast("Data upload failed: Missing or invalid data detected. Please ensure that student-data for the selected semester, course-data and course-offerings is valid.", statusColors.BAD_REQUEST);
            }
            break;
        default:
            statusDiv.className = "bg-yellow-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
            statusText.textContent = "Yet to run";
            if (showToastForSemester) {
                showToast("Something went wrong! Please contact support.", statusColors.ERROR);
            }
            break;
    }

    allocatedDiv.textContent = data.allocated;
    unallocatedDiv.textContent = data.unallocated;
    sessionStorage.removeItem(`showToastForSemester_${sem}`);
}

document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.semester-btn');
    const hiddenInput = document.getElementById('selectedSemester');
    const form = document.getElementById('allocationForm');
    const executeBtn = document.getElementById('executeBtn');

    // Use semester value from backend
    let selectedSemester = semester || 5; // fallback to 5 if somehow undefined
    hiddenInput.value = selectedSemester;

    // Style semester buttons based on selection
    buttons.forEach(btn => {
        const sem = parseInt(btn.getAttribute('data-sem'));
        btn.style.backgroundColor = sem === selectedSemester ? customColors.DARK_GREEN : customColors.COBALT_BLUE;

        btn.addEventListener('click', () => {
            selectedSemester = sem;
            hiddenInput.value = sem;

            buttons.forEach(b => b.style.backgroundColor = customColors.COBALT_BLUE);
            btn.style.backgroundColor = customColors.DARK_GREEN;

            updateAllocationSummary(sem);
        });
    });

    // ✅ Show allocation summary for selected semester on page load
    updateAllocationSummary(selectedSemester);

    // ✅ Intercept form submission if needed
    form.addEventListener('submit', function (e) {
        if (registrationStatus === 'open') {
            e.preventDefault();
            openCloseRegModal();
        } else {

            const spinner = document.getElementById('spinner');
            const executeBtn = document.getElementById('executeBtn');
            const executeIcon = document.getElementById('executeIcon');
            const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');

            // Show toast flag
            sessionStorage.setItem(`showToastForSemester_${hiddenInput.value}`, "true");

            this.setAttribute('action', `${contextPath}admin/execute-allocation/${hiddenInput.value}`);

            // Show spinner and hide icon
            spinner.classList.remove('hidden');
            if (executeIcon) executeIcon.classList.add('hidden');

            // Disable the button to prevent multiple submissions
            executeBtn.disabled = true;
            executeBtn.classList.add('opacity-50', 'cursor-not-allowed');
            executeBtn.classList.remove('opacity-100', 'cursor-pointer');
        }
    });
});

function openCloseRegModal() {
    document.getElementById("closeRegModal").classList.remove("hidden");
}

function closeCloseRegModal() {
    document.getElementById("closeRegModal").classList.add("hidden");
}


function handleExecuteConfirmation() {
    closeCloseRegModal();

    const form = document.getElementById('allocationForm');
    const semester = document.getElementById('selectedSemester').value;
    const spinner = document.getElementById('spinner');
    const executeBtn = document.getElementById('executeBtn');
    const executeIcon = document.getElementById('executeIcon');
    const contextPath = document.querySelector('meta[name="context-path"]').getAttribute('content');


    // Show toast flag
    sessionStorage.setItem(`showToastForSemester_${semester}`, "true");

    // Set action based on selected semester
    form.setAttribute('action', `${contextPath}admin/execute-allocation/${semester}`);

    // Show spinner and hide icon
    spinner.classList.remove('hidden');
    if (executeIcon) executeIcon.classList.add('hidden');

    // Disable the button to prevent multiple submissions
    executeBtn.disabled = true;
    executeBtn.classList.add('opacity-50', 'cursor-not-allowed');
    executeBtn.classList.remove('opacity-100', 'cursor-pointer');

    // Submit the form
    form.submit();
}
