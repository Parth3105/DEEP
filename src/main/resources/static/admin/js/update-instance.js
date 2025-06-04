// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
  menu.classList.toggle('hidden');
});

// Send the uploaded file to backend
const HandleUpload = async (type) => {
    const input = document.getElementById(`fileInput-${type}`);
    const messageBox = document.getElementById(`uploadMessage-${type}`);

    if (!input) {
        showMessage(messageBox, 'Input element not found.', true);
        return;
    }

    const file = input.files?.[0];

    if (!file) {
        showMessage(messageBox, 'Please select a file first.', true);
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(`/upload/${type}`, {
            method: 'POST',
            body: formData
        });

        const text = await response.text();

        if(true) {
            const acknowledgment = "File uploaded successfully!";
            showMessage(messageBox, acknowledgment, false);
        } else {
            const isError = !response.ok;
            showMessage(messageBox, isError ? `Error: Internal Server Error!` : text, isError);
        }

    } catch (error) {
        showMessage(messageBox, 'Upload failed: ' + error.message, true);
    }
};

// Utility to show and auto-hide message
function showMessage(element, message, isError) {
    if (!element) return;

    element.textContent = message;
    element.classList.toggle('text-red-600', isError);
    element.classList.toggle('text-green-600', !isError);

    // Clear after 2 seconds
    setTimeout(() => {
        element.textContent = '';
        element.classList.remove('text-red-600', 'text-green-600');
    }, 2000);
}

// Enabling submit button only if at least one file is selected
const fileInputIds = ['fileInput-students', 'fileInput-offerings', 'fileInput-instituteRequirements', 'fileInput-courses'];
const submitBtn = document.getElementById('create-instance');

function checkFiles() {
    const anyFileSelected = fileInputIds.some(id => {
        const input = document.getElementById(id);
        return input && input.files.length > 0;
    });

    submitBtn.disabled = !anyFileSelected;
    submitBtn.classList.toggle('opacity-50', !anyFileSelected);
    submitBtn.classList.toggle('cursor-not-allowed', !anyFileSelected);
}

// Attach listeners to all file inputs
fileInputIds.forEach(id => {
    const input = document.getElementById(id);
    if (input) {
        input.addEventListener('change', checkFiles);
    }
});

// Initial check in case user reloads with a file already selected
checkFiles();

function handleSubmit(event) {
    const button = document.getElementById("create-instance");
    const text = document.getElementById("button-text");
    const spinner = document.getElementById("spinner");

    button.disabled = true;

    text.textContent = "This may take a while!";
    spinner.classList.remove("hidden");
}
