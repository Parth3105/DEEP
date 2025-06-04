// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
  menu.classList.toggle('hidden');
});

function openModal() {
    document.getElementById('create-instance-modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('create-instance-modal').classList.add('hidden');
}

function HandleRoute(url) {
    window.location.href = url;
}

const form = document.getElementById('createForm');
const btnText = document.getElementById('submitBtnText');
const spinner = document.getElementById('spinner');
const submitBtn = document.getElementById('submitBtn');

form.addEventListener('submit', function (e) {
    if (!form.checkValidity()) return;

    // Otherwise, show loading state
    btnText.textContent = "This may take a while!";
    spinner.classList.remove("hidden");
    submitBtn.disabled = true;
});