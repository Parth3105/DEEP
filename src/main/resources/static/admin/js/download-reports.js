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

        document.querySelectorAll('.result-semester-input').forEach(input => {
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