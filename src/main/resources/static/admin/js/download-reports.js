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

let selectedSemester = null;
function HandleSemesterSelection(semesterBtns, downloadBtns, semesterInputs) {
    semesterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // Remove active state from all buttons
            semesterBtns.forEach(b => {
                b.style.backgroundColor = '#1E3C72';
            });

            // Set active state for clicked button
            this.style.backgroundColor = '#2D9D5D';
            selectedSemester = this.dataset.sem;

            // Update all hidden semester inputs
            semesterInputs.forEach(input => {
                input.value = selectedSemester;
            });

            // Enable download buttons
            downloadBtns.forEach(downloadBtn => {
                downloadBtn.disabled = false;
                downloadBtn.style.opacity = '1';
                downloadBtn.style.cursor = 'pointer';
            });
        });
    });
}

function InitializeDownloadButtons(resultDownloadBtns) {
    if (selectedSemester) {
        resultDownloadBtns.forEach(btn => {
            btn.disabled = false;
        });

        document.querySelectorAll('.result-semester-input').forEach(input => {
            input.value = selectedSemester;
        });
    }
}

function HandleDownloadButtonClick(downloadBtns, checkforSemester = true) {
    downloadBtns.forEach(btn => {
        btn.addEventListener('click', async function (e) {
            e.preventDefault();

            if (checkforSemester && !selectedSemester) {
                showToast('Please select a semester first!');
                return;
            }

            const form = this.closest('form');
            const name = form.querySelector('input[name="name"]').value;
            const semester = checkforSemester ? selectedSemester : '';
            const downloadUrl = checkforSemester
                ? `/download-reports/${semester}/${name}`
                : `/download-reports/${name}`;

            // Show loading state
            const originalText = this.innerHTML;
            this.innerHTML = `
                <span class="animate-spin inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full mr-2"></span>
                Downloading...`;
            this.disabled = true;

            fetch(downloadUrl)
              .then(res => {
                console.log(res);
                if (!res.ok) {
                  // Handle error response
                  return res.text().then(text => {
                    console.error("Download failed:", text);
                    showToast("Download failed: " + (text || "Unknown error"));
                  });
                }

                // If response is OK, we must trigger a file download separately
                return res.blob().then(blob => {
                  const url = window.URL.createObjectURL(blob);
                  const a = document.createElement('a');
                  a.href = url;

                  // Extract filename from URL or fallback
                  const filename = downloadUrl.split('/').pop() || 'downloaded_file';
                  a.download = filename;
                  document.body.appendChild(a);
                  a.click();
                  a.remove();
                  window.URL.revokeObjectURL(url);
                });
              })
              .catch(err => {
                console.error("Fetch error:", err);
                showToast("Network or server error occurred.");
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
    // Handle Input Data
    const inputDownloadBtns = document.querySelectorAll('.input-download-btn');
    HandleDownloadButtonClick(inputDownloadBtns, false);

    // Handle result files
    const resultSemesterBtns = document.querySelectorAll('.result-semester-btn');
    const resultDownloadBtns = document.querySelectorAll('.result-download-btn');
    const resultSemesterInputs = document.querySelectorAll('.result-semester-input');

    HandleSemesterSelection(resultSemesterBtns, resultDownloadBtns, resultSemesterInputs);
    InitializeDownloadButtons(resultDownloadBtns);
    HandleDownloadButtonClick(resultDownloadBtns);
});