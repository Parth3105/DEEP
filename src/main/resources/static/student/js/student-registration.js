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
    toast.classList.remove("bg-red-500", "bg-yellow-400");

    // Apply based on type
    if (type === 'error') {
        toast.classList.add("bg-red-500");
    } else if (type === 'warning') {
        toast.classList.add("bg-yellow-400", "text-gray-900");
    }

    toast.classList.remove("hidden");
    toast.classList.add("flex");

    setTimeout(() => {
        hideToast();
    }, 3000);
}

function hideToast() {
    const toast = document.getElementById("toast-error");
    toast.classList.remove("flex");
    toast.classList.add("hidden");
}

const categories = ['ICTE', 'TE', 'SE', 'MNCE', 'OE'];
const values = {};

categories.forEach(cat => {
    values[cat] = '0';

    const input = document.querySelector(`input[name="${cat}"]`);
    if (input) {
    input.addEventListener('input', () => {
        values[cat] = input.value.trim() || '0';
    });
    }
});

// Registration Form Steps
let currentStep = 1;
const totalSteps = 3;

function showStep(step) {
    for (let i = 1; i <= totalSteps; i++) {
        const el = document.getElementById(`step-${i}`);
        const progressEL = document.getElementById(`progress-${i}`);
        const progressLine = document.getElementById(`line-${i}`);

        if (progressEL) {
            progressEL.classList.toggle('bg-[#1321EA]', i < step);
            progressEL.classList.toggle('bg-[#ACCEFF]', i > step);

            if (i === step) {
                progressEL.style.boxShadow = 'inset 0 0 0 5px #1321EA';
            } else {
                progressEL.style.boxShadow = '';
            }
        }

        if(progressLine) {
            progressLine.classList.toggle('bg-[#1321EA]', i <= step);
            progressLine.classList.toggle('bg-[#ACCEFF]', i > step);
        }

        if (el) {
            el.classList.toggle('hidden', i !== step);
        }
    }
}

function CheckInputs() {
    if(currentStep === 2) {
        console.log(selectedCoursesBySlot);
    }
    if(currentStep !== 1) {
        return true;
    }

    const element = document.getElementById('step-1');
    const inputs = element.querySelectorAll('input[type="number"]');

    for (let input of inputs) {
        if (input.value.trim() === '') {
            showToast("Please, Fill all 4 requirements First!");
            return false;
        }
    }

    return true;
}

function nextStep() {
    if (CheckInputs() && currentStep < totalSteps) {
        currentStep++;
        showStep(currentStep);
    }
}

function prevStep() {
    if (currentStep > 1) {
    currentStep--;
    showStep(currentStep);
    }
}

// Registration Form 2
let selectedCoursesBySlot = {};
let currentSlot = '';

// Show courses for a specific slot
function showSlotCourses(slot) {
    slot = String(slot);
    if (slot === currentSlot) return;

    const currentSlotCourses = document.querySelectorAll(`#slot-${currentSlot} .course-row`);
    const selectedCIDsInCurrent = selectedCoursesBySlot[currentSlot] || [];
    const totalCoursesInCurrent = currentSlotCourses.length;

    const noSlotCourseCheckbox = document.getElementById('noSlotCourse');

    const hasAllSelected = selectedCIDsInCurrent.length === totalCoursesInCurrent;

    if (currentStep === 2 && !hasAllSelected && (!noSlotCourseCheckbox || !noSlotCourseCheckbox.checked)) {
        showToast(`Please select all courses or confirm you don't want any from Slot ${currentSlot}.`, "warning");
        return;
    }

    // Slot switch is allowed
    currentSlot = slot;

    if (noSlotCourseCheckbox) noSlotCourseCheckbox.checked = false;

    // Hide all slot courses
    document.querySelectorAll('.slot-courses').forEach(el => el.classList.add('hidden'));

    // Show selected slot
    const slotElement = document.getElementById('slot-' + slot);
    if (slotElement) slotElement.classList.remove('hidden');

    // Update active styles
    const slots = document.querySelectorAll("#slotContainer .slot");
    slots.forEach(s => {
        s.classList.remove("bg-blue-500", "text-white");
        s.classList.add("bg-cyan-300", "text-gray-800");
    });

    const selectedSlotBtn = document.querySelector(`.slot[data-slot="${slot}"]`);
    if (selectedSlotBtn) {
        selectedSlotBtn.classList.remove("bg-cyan-300", "text-gray-800");
        selectedSlotBtn.classList.add("bg-blue-500", "text-white");
    }

    updateSelectedCoursesDisplay();
    updateCourseVisibility();
}

// Hide already selected courses in current slot
function updateCourseVisibility() {
    document.querySelectorAll('.course-row').forEach(courseRow => {
        const courseId = courseRow.getAttribute('data-course-id');
        const slot = courseRow.getAttribute('data-slot');

        const selectedInSlot = selectedCoursesBySlot[slot] || [];
        const isSelected = selectedInSlot.some(course => course.cid === courseId);

        courseRow.style.display = (slot === currentSlot && !isSelected) ? 'grid' : 'none';
    });
}

function addCourseToSelected(cid, slot, name, program, category, credits) {
    if (!selectedCoursesBySlot[slot]) {
        selectedCoursesBySlot[slot] = [];
    }

    if (selectedCoursesBySlot[slot].find(course => course.cid === cid)) {
        alert('Course already selected in this slot!');
        return;
    }

    selectedCoursesBySlot[slot].push({ cid, slot, name, program, category, credits });

    updateSelectedCoursesDisplay();
    updateCourseVisibility();
}

function removeCourseFromSelected(cid) {
    const coursesInSlot = selectedCoursesBySlot[currentSlot] || [];
    selectedCoursesBySlot[currentSlot] = coursesInSlot.filter(course => course.cid !== cid);

    updateSelectedCoursesDisplay();
    updateCourseVisibility();
}

function updateSelectedCoursesDisplay() {
    const selectedCoursesContainer = document.getElementById('selectedCourses');
    const courses = selectedCoursesBySlot[currentSlot] || [];

    if (courses.length === 0) {
        selectedCoursesContainer.innerHTML = '<div class="h-4"></div>';
        return;
    }

    let html = '<div class="h-4"></div>';
    courses.forEach(course => {
        html += `
            <div class="grid grid-cols-7 gap-0 text-[8px] md:text-xs lg:text-sm border-b border-gray-300 bg-green-100">
                <div class="p-2 lg:p-3 flex justify-center items-center">
                    <button class="w-4 h-4 md:w-6 md:h-6 bg-red-500 pb-[1px] md:pb-1 rounded-full flex items-center justify-center text-white cursor-pointer font-bold hover:bg-red-600 transition-colors"
                            onclick="removeCourseFromSelected('${course.cid}')">
                        -
                    </button>
                </div>
                <div class="p-2 lg:p-3 text-center font-medium">${course.cid}</div>
                <div class="p-2 lg:p-3 col-span-2 font-medium">${course.name}</div>
                <div class="p-2 lg:p-3 text-center font-medium">${course.program}</div>
                <div class="p-2 lg:p-3 text-center font-medium">${course.category}</div>
                <div class="p-2 lg:p-3 text-center font-medium">${course.credits}</div>
            </div>
        `;
    });

    selectedCoursesContainer.innerHTML = html;
}

// Initialization
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.slot[data-slot]').forEach(slotBtn => {
        slotBtn.addEventListener('click', function () {
            const slot = this.getAttribute('data-slot');
            showSlotCourses(slot);
        });
    });

    document.querySelectorAll('.course-btn').forEach(courseBtn => {
        courseBtn.addEventListener('click', function () {
            const cid = this.getAttribute('data-cid');
            const slot = this.getAttribute('data-slot');
            const name = this.getAttribute('data-name');
            const program = this.getAttribute('data-program');
            const category = this.getAttribute('data-category');
            const credits = parseInt(this.getAttribute('data-credits'));
            addCourseToSelected(cid, slot, name, program, category, credits);
        });
    });

    // Set first slot as active initially
    const firstSlot = document.querySelector('.slot[data-slot]');
    if (firstSlot) {
        showSlotCourses(firstSlot.getAttribute('data-slot'));
    }
});

// Registration Form 3
function collectPreferences() {
    const inputs = document.querySelectorAll('.slot-preference-input');
    const preferences = Array.from(inputs).map(input => input.value.trim());
    console.log('User Preferences:', preferences);
    return preferences;
}

function getSlotPrefsToString(slotPrefs) {
    return slotPrefs.join('$');
}

function getCoursePrefsToString(coursePrefs) {
  return Object.entries(coursePrefs)
    .map(([slotId, courses]) => {
      const cids = courses.map(course => course.cid).join('$');
      return `${slotId}:${cids}`;
    })
    .join('#');
}

function getAcadReqToString(obj) {
  return Object.entries(obj)
    .map(([key, value]) => `${key}:${value}`)
    .join('#');
}

document.getElementById('submitButton').addEventListener('click', function () {
    const acad = getAcadReqToString(values);
    const course = getCoursePrefsToString(selectedCoursesBySlot);
    const slot = getSlotPrefsToString(collectPreferences());

    document.getElementById('studentRequirements').value = acad;
    document.getElementById('coursePreferences').value = course;
    document.getElementById('slotPreferences').value = slot;

    document.getElementById('myForm').submit();
});