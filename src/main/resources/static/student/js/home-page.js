// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
    menu.classList.toggle('hidden');
});

function HandleRoute(url) {
    window.location.href = url;
}