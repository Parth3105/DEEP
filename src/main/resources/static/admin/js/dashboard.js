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