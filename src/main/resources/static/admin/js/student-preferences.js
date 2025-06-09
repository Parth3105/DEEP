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

let selectedSemester = null;
function HandleSemesterSelection(semesterBtns, downloadBtns, semesterInputs) {
    semesterBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            // Remove active state from all buttons
            semesterBtns.forEach(b => {
                b.style.backgroundColor = '#1E3C72';
            });

            // Set active state for clicked button
            this.style.backgroundColor = '#2D9D5D';
            const selectedSemester = this.dataset.sem;

            // Update all hidden semester inputs
            semesterInputs.forEach(input => {
                input.value = selectedSemester;
            });

            // Enable download buttons
            downloadBtns.forEach(downloadBtn => {
                downloadBtn.disabled = false;
                downloadBtn.classList.remove('opacity-50', 'cursor-not-allowed');
                downloadBtn.classList.add('cursor-pointer');
            });
        });
    });
}

function InitializeDownloadButtons(resultDownloadBtns) {
    if (selectedSemester) {
        resultDownloadBtns.forEach(btn => {
            btn.disabled = false;
        });

        document.querySelectorAll('.semester-input').forEach(input => {
            input.value = selectedSemester;
        });
    }
}

function printErrorMessage(status, errorText) {
    if(status === 404)
        showToast("Download failed: " + (errorText || "Unknown error"), "warning");
    else if(status === 500)
        showToast("Download failed: " + (errorText || "Unknown error"));
    else
        showToast("Download failed due to an Unknown error! Please contact support.");
}

function HandleDownloadButtonClick(downloadBtns) {
    downloadBtns.forEach(btn => {
        btn.addEventListener('click', async function (e) {
            e.preventDefault();

            if (!selectedSemester) {
                showToast('Please select a semester first!');
                return;
            }

            const form = this.closest('form');
            const name = form.querySelector('input[name="name"]').value;
            const semester = selectedSemester;
            const downloadUrl = `/admin/student-preferences/download/${semester}`;
            console.log(downloadUrl);

            // Show loading state
            const originalText = this.innerHTML;
            this.innerHTML = `
                <span class="animate-spin inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full mr-2"></span>
                Downloading...`;
            this.disabled = true;

            fetch(downloadUrl)
              .then(async res => {
                if (res.status !== 200) {
                  const errorText = await res.text();
                  printErrorMessage(res.status, errorText);
                  return;
                }

                const blob = await res.blob();
                const contentDisposition = res.headers.get("Content-Disposition");

                // Try to extract filename from Content-Disposition header
                let filename = "downloaded_file";
                if (contentDisposition && contentDisposition.includes("filename=")) {
                  const match = contentDisposition.match(/filename="?([^"]+)"?/);
                  if (match && match[1]) filename = match[1];
                } else {
                  filename = downloadUrl.split("/").pop() || filename;
                }

                const url = window.URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
              })
              .catch(err => {
                console.error("Fetch error:", err);
                showToast("Something went wrong due to Network Error. Please contact support.");
              });

            // Restore button
            setTimeout(() => {
                this.innerHTML = originalText;
                this.disabled = false;
            }, 2000);
        });
    });
}

document.addEventListener('DOMContentLoaded', function() {
    // Handle result files
    const resultSemesterBtns = document.querySelectorAll('.semester-btn');
    const resultDownloadBtns = document.querySelectorAll('.download-btn');
    const resultSemesterInputs = document.querySelectorAll('.semester-input');

    HandleSemesterSelection(resultSemesterBtns, resultDownloadBtns, resultSemesterInputs);
    InitializeDownloadButtons(resultDownloadBtns);
    HandleDownloadButtonClick(resultDownloadBtns);
});

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
        window.location.href = `/admin/student-preferences/${encodeURIComponent(sid)}`;
    }
}

const categoryLabels = {
    'ICTE': 'ICT Electives',
    'TE': 'Technical Electives',
    'SE': 'Science Electives',
    'MNCE': 'MNCE Electives',
    'OE': 'Open Electives',
    'HSSE': 'Humanities and Social Sciences Electives'
};

document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("requirements-container");

    if(!studentRequirements) return;
    studentRequirements.forEach(req => {
        const label = categoryLabels[req.category] || 'Other';
        const courseCount = req.course_cnt;

        // Outer div with class "flex"
        const outerDiv = document.createElement("div");
        outerDiv.className = "flex mb-1";

        // Inner left div with label and colon
        const leftDiv = document.createElement("div");
        leftDiv.className = "flex justify-between";

        const labelSpan = document.createElement("span");
        labelSpan.className = "w-46";
        labelSpan.textContent = label;

        const colonSpan = document.createElement("span");
        colonSpan.className = "mr-3";
        colonSpan.textContent = ":";

        leftDiv.appendChild(labelSpan);
        leftDiv.appendChild(colonSpan);

        // Right span with course count
        const countSpan = document.createElement("span");
        countSpan.textContent = courseCount;

        // Append both parts to outer div
        outerDiv.appendChild(leftDiv);
        outerDiv.appendChild(countSpan);

        // Append to container
        container.appendChild(outerDiv);
    });
});

// Add interactivity for collapsible slots
document.addEventListener('DOMContentLoaded', function() {
    const slots = document.querySelectorAll('[class*="bg-blue-200"]');

    slots.forEach(slot => {
        slot.addEventListener('click', function() {
            const arrow = this.querySelector('img');
            const content = this.nextElementSibling;
            if (content && content.classList.contains('course-list')) {
                this.classList.contains('rounded-b-xl') ? this.classList.remove('rounded-b-xl') : this.classList.add('rounded-b-xl');
                content.style.display = content.style.display === 'none' ? 'block' : 'none';
                arrow.src = arrow.src.includes('close.svg') ? '/student/images/open.svg' : '/student/images/close.svg';
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const groupedContainer = document.getElementById('groupedCoursePrefs');

    // Group by slot
    if(!coursePreferences) return;
    const grouped = {};
    coursePreferences.forEach(cp => {
        if (!grouped[cp.slot]) grouped[cp.slot] = [];
        grouped[cp.slot].push(cp);
    });

    // Sort slot keys (if numeric)
    const sortedSlots = Object.keys(grouped).sort((a, b) => parseInt(a) - parseInt(b));

    // Render each slot group
    sortedSlots.forEach(slot => {
        const slotGroup = grouped[slot];

        const wrapper = document.createElement('div');

        wrapper.innerHTML = `
            <div class="mb-4">
                <div class="bg-blue-200 rounded-t-xl px-6 py-2 text-base font-bold text-gray-800 flex justify-between items-center cursor-pointer toggle-header">
                    <span>Slot-${slot}</span>
                    <img src="/student/images/close.svg" alt="Toggle" class="w-4 h-4 rotate-icon">
                </div>
                <div class="course-list bg-blue-50 px-6 py-3 text-sm md:text-base space-y-1 rounded-b-xl">
                    ${slotGroup.map(cp => `
                        <div class="border-b border-gray-200 pb-2">Preference - ${cp.pref} : ${cp.cname} (${cp.cid})</div>
                    `).join('')}
                </div>
            </div>
        `;

        groupedContainer.appendChild(wrapper);
    });

    // Toggle functionality
    document.querySelectorAll('.toggle-header').forEach(header => {
        header.addEventListener('click', () => {
            const courseList = header.nextElementSibling;
            const icon = header.querySelector('.rotate-icon');
            courseList.classList.toggle('hidden');
            icon.classList.toggle('rotate-180');
        });
    });
});