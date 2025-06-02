// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
  menu.classList.toggle('hidden');
});

// File upload functionality
// document.querySelectorAll('.upload-btn').forEach(btn => {
//   btn.addEventListener('click', function () {
//     const input = document.createElement('input');
//     input.type = 'file';
//     input.accept = '.xlsx,.xls';
//     input.click();

//     input.addEventListener('change', function () {
//       if (this.files.length > 0) {
//         btn.textContent = `Selected: ${this.files[0].name}`;
//         btn.classList.remove('bg-indigo-600', 'hover:bg-indigo-700');
//         btn.classList.add('bg-green-600', 'hover:bg-green-700');
//       }
//     });
//   });
// });

// Create Instance button functionality
// document.getElementById('create-instance').addEventListener('click', function () {
//   this.textContent = 'Creating Instance...';
//   this.classList.remove('bg-green-600', 'hover:bg-green-700');
//   this.classList.add('bg-gray-600', 'cursor-not-allowed');
//   this.disabled = true;

//   setTimeout(() => {
//     this.textContent = 'Instance Created!';
//     this.classList.remove('bg-gray-600');
//     this.classList.add('bg-green-600');

//     setTimeout(() => {
//       this.textContent = 'Create Instance';
//       this.classList.add('hover:bg-green-700');
//       this.classList.remove('cursor-not-allowed');
//       this.disabled = false;
//     }, 2000);
//   }, 1500);
// });

// Add some interactive hover effects
// document.querySelectorAll('.upload-btn').forEach(btn => {
//   btn.addEventListener('mouseenter', function () {
//     this.style.transform = 'scale(1.05) translateY(-2px)';
//   });

//   btn.addEventListener('mouseleave', function () {
//     this.style.transform = 'scale(1) translateY(0)';
//   });
// });