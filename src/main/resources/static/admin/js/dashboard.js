document.addEventListener("DOMContentLoaded", function () {
  const toggle = document.getElementById("toggleRegistration");
  const modal = document.getElementById("registrationModal");

  toggle.addEventListener("change", function () {
    if (this.checked) {
      modal.classList.remove("hidden");
    }
  });

  window.closeModal = function () {
    modal.classList.add("hidden");
    toggle.checked = false;
  };
});

function handleOpenRegistration(event) {
  event.preventDefault();

  const dateInput = document.getElementById("registration-datepicker");
  const rawValue = dateInput.value.trim();

  if (!rawValue) {
    showToast("Please select a close date.", 'warning');
    return;
  }

  // Parse to Date object
  const parsedDate = new Date(rawValue);
  if (isNaN(parsedDate.getTime())) {
    showToast("Invalid date selected.");
    return;
  }

  // Format to YYYY-MM-DD
  const year = parsedDate.getFullYear();
  const month = String(parsedDate.getMonth() + 1).padStart(2, "0");
  const day = String(parsedDate.getDate()).padStart(2, "0");
  const formattedDate = `${year}-${month}-${day}`;

  // Set the action with query param
  const form = event.target;
  form.action = `/admin/open-registration?close-date=${encodeURIComponent(formattedDate)}`;
  form.submit();
}

  function openExtendModal() {
    document.getElementById("extendModal").classList.remove("hidden");
  }

  function closeExtendModal() {
    document.getElementById("extendModal").classList.add("hidden");
  }

  function handleExtend(event) {
    event.preventDefault();

    const input = document.getElementById("extend-datepicker");
    const rawValue = input.value.trim();

    if (!rawValue) {
      showToast("Please select a new close date.", 'warning');
      return;
    }

    const parsedDate = new Date(rawValue);
    if (isNaN(parsedDate.getTime())) {
      showToast("Invalid date.");
      return;
    }

    const year = parsedDate.getFullYear();
    const month = String(parsedDate.getMonth() + 1).padStart(2, "0");
    const day = String(parsedDate.getDate()).padStart(2, "0");
    const formattedDate = `${year}-${month}-${day}`;

    const form = event.target;
    form.action = `/admin/extend-period?close-date=${encodeURIComponent(formattedDate)}`;
    form.submit();
  }

function closeModal() {
    modal.classList.add('hidden');
    // Uncheck the toggle if Cancel is clicked
    registrationToggle.checked = false;
}

function openModal() {
    document.getElementById('create-instance-modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('create-instance-modal').classList.add('hidden');
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