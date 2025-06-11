const allocationStatusMap = {};
allocationStatusList?.forEach(entry => {
    allocationStatusMap[entry.semester] = {
        allocated: entry.allocated,
        unallocated: entry.unallocated,
        allocationstatus: entry.allocationstatus
    };
});

function updateAllocationSummary(sem) {
    const data = allocationStatusMap?.[sem] || {
        allocated: "-",
        unallocated: "-",
        allocationstatus: "204"
    };

    const statusDiv = document.getElementById("allocation-status");
    const statusText = document.getElementById("allocation-status-text");
    const allocatedDiv = document.getElementById("allocated-count");
    const unallocatedDiv = document.getElementById("unallocated-count");

    const status = data.allocationstatus;

    if (status === "204") {
        statusDiv.className = "bg-yellow-400 text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Yet to run";
    } else if (status === "200") {
        statusDiv.className = "bg-gradient-to-r from-[#27AE60] to-[#2ECC71] text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Allocation Successful";
    } else if (status === "500") {
        statusDiv.className = "bg-red-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Failed";
        showToast("Internal server error during allocation.");
    } else {
        statusDiv.className = "bg-gray-500 text-white px-7 py-2 rounded-xl font-medium text-lg";
        statusText.textContent = "Unknown";
    }

    allocatedDiv.textContent = data.allocated;
    unallocatedDiv.textContent = data.unallocated;
}

//const registrationStatus = 'close';
document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.semester-btn');
    const hiddenInput = document.getElementById('selectedSemester');
    const form = document.getElementById('allocationForm');
    const executeBtn = document.getElementById('executeBtn');

    let selectedSemester = 5;
    if ([5, 6, 7, 8].includes(parseInt(hiddenInput.value))) {
        selectedSemester = parseInt(hiddenInput.value);
    }
    hiddenInput.value = selectedSemester;

    // Style buttons and attach listeners
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

    // Initial allocation summary
    updateAllocationSummary(selectedSemester);

    // 🔁 Re-apply active styling to selected semester button
    buttons.forEach(btn => {
        const sem = parseInt(btn.getAttribute('data-sem'));
        btn.style.backgroundColor = sem === selectedSemester ? customColors.DARK_GREEN : customColors.COBALT_BLUE;
    });

    // Intercept form submission to conditionally show modal
    form.addEventListener('submit', function (e) {
        if (registrationStatus === 'open') {
            e.preventDefault(); // prevent normal submission
            openCloseRegModal(); // show modal
        } else {
            const semester = hiddenInput.value;
            this.setAttribute('action', `/admin/execute-allocation/${semester}`);
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

    // Now allow form to submit with correct semester
    const form = document.getElementById('allocationForm');
    const semester = document.getElementById('selectedSemester').value;
    form.setAttribute('action', `/admin/execute-allocation/${semester}`);
    form.submit();
}
