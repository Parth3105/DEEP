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
    window.location.href = `/admin/${url}`;
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

  const prefCountEl = document.getElementById("prefCount");
  const allocStatusEl = document.getElementById("allocStatus");
  const buttons = document.querySelectorAll(".semester-btn");

  buttons.forEach(button => {
    button.addEventListener("click", () => {
      // Style toggle
      buttons.forEach(b => {
        b.classList.remove("bg-[#46A24A]");
        b.classList.add("bg-[#22437E]");
      });
      button.classList.remove("bg-[#22437E]");
      button.classList.add("bg-[#46A24A]");

      const sem = parseInt(button.dataset.semester);
      const semData = registrationStatus.find(r => r.semester === sem);

      if (semData) {
        prefCountEl.textContent = `${semData.submitted_prefereces} / ${semData.total_students}`;
        allocStatusEl.textContent = semData.allocation_status || "Pending";
      }
    });
  });

  // Select Semester 5 by default
  window.addEventListener("DOMContentLoaded", () => {
    document.querySelector('.semester-btn[data-semester="5"]')?.click();
  });

const registrationToggle = document.getElementById('toggleRegistration');
const modal = document.getElementById('registrationModal');

registrationToggle.addEventListener('change', (e) => {
  // If turned on, show confirmation modal
  if (e.target.checked) {
    modal.classList.remove('hidden');
  }
});

function closeModal() {
  modal.classList.add('hidden');
  // Uncheck the toggle if Cancel is clicked
  registrationToggle.checked = false;
}