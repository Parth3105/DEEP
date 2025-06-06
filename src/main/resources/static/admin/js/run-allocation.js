// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
  menu.classList.toggle('hidden');
});

const buttons = document.querySelectorAll('.semester-btn');
const selectedInput = document.getElementById('selectedSemester');
const form = document.getElementById('allocationForm');

// Handle semester button clicks and styling
buttons.forEach(button => {
    button.addEventListener('click', () => {
      buttons.forEach(btn => {
        btn.classList.remove('bg-[#2D9D5D]');
        btn.classList.add('bg-[#1E3C72]');
      });

      button.classList.remove('bg-[#1E3C72]');
      button.classList.add('bg-[#2D9D5D]');

      selectedInput.value = button.getAttribute('data-sem');
    });
});

// Before form submission, set the action dynamically with the semester
form.addEventListener('submit', (event) => {
    const sem = selectedInput.value;
    console.log(sem);
//    form.action = `/execute-allocation/${sem}`;
    // form will submit normally with POST method
});