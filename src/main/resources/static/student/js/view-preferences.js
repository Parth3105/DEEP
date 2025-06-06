// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
    menu.classList.toggle('hidden');
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