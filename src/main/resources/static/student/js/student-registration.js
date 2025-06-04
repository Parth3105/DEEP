// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
    menu.classList.toggle('hidden');
});

// Toast Notification
function showToast(message) {
    const toast = document.getElementById("toast-error");
    const text = document.getElementById("toast-message");

    text.innerText = message;
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
const slots = document.querySelectorAll("#slotContainer .slot");
slots.forEach(slot => {
slot.addEventListener("click", () => {
    // Remove active style from all
    slots.forEach(s => {
    s.classList.remove("bg-blue-500", "text-white");
    s.classList.add("bg-cyan-300", "text-gray-800");
    });

    // Apply active style to clicked one
    slot.classList.remove("bg-cyan-300", "text-gray-800");
    slot.classList.add("bg-blue-500", "text-white");
});
});

// Course management functionality
let selectedCourses = [];
document.addEventListener('DOMContentLoaded', function() {
    attachEventListeners();
});

function attachEventListeners() {
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('course-btn')) {
            e.preventDefault();
            const courseRow = e.target.closest('.course-row');
            const courseId = courseRow.dataset.courseId;

            if (e.target.textContent.trim() === '+') {
                addCourse(courseRow, courseId);
            } else if (e.target.textContent.trim() === '-') {
                removeCourse(courseRow, courseId);
            }
        }
    });
}

function addCourse(courseRow, courseId) {
    selectedCourses.push(courseId);
    const clonedRow = courseRow.cloneNode(true);

    const button = clonedRow.querySelector('.course-btn');
    button.textContent = '-';
    button.className = 'course-btn w-4 h-4 md:w-6 md:h-6 bg-red-500 pb-[1px] md:pb-1 rounded-full flex items-center justify-center text-white cursor-pointer font-bold hover:bg-red-600 transition-colors';

    clonedRow.className = clonedRow.className.replace('border-b border-gray-300', '');
    courseRow.remove();

    const selectedCoursesDiv = document.getElementById('selectedCourses');
    const existingSelectedRows = selectedCoursesDiv.querySelectorAll('.course-row');
    if (existingSelectedRows.length > 0) {
        const lastRow = existingSelectedRows[existingSelectedRows.length - 1];
        if (!lastRow.classList.contains('border-b')) {
            lastRow.classList.add('border-b', 'border-gray-300');
        }
    }

    selectedCoursesDiv.appendChild(clonedRow);

    // console.log('Course added:', courseId);
    // console.log('Selected courses:', selectedCourses);
}

function removeCourse(courseRow, courseId) {
    selectedCourses = selectedCourses.filter(id => id !== courseId);
    const clonedRow = courseRow.cloneNode(true);

    const button = clonedRow.querySelector('.course-btn');
    button.textContent = '+';
    button.className = 'course-btn w-4 h-4 md:w-6 md:h-6 bg-green-500 pb-[1px] md:pb-1 rounded-full flex items-center justify-center text-white cursor-pointer font-bold hover:bg-green-600 transition-colors';
    courseRow.remove();

    const selectedCoursesDiv = document.getElementById('selectedCourses');
    const remainingSelectedRows = selectedCoursesDiv.querySelectorAll('.course-row');
    if (remainingSelectedRows.length > 0) {
        const lastRow = remainingSelectedRows[remainingSelectedRows.length - 1];
        lastRow.classList.remove('border-b', 'border-gray-300');
    }

    const availableCoursesDiv = document.getElementById('availableCourses');
    const existingRows = availableCoursesDiv.querySelectorAll('.course-row');

    let insertPosition = null;
    for (let i = 0; i < existingRows.length; i++) {
        const existingId = parseInt(existingRows[i].dataset.courseId);
        const currentId = parseInt(courseId);
        if (currentId < existingId) {
            insertPosition = existingRows[i];
            break;
        }
    }

    if (insertPosition) {
        clonedRow.classList.add('border-b', 'border-gray-300');
        availableCoursesDiv.insertBefore(clonedRow, insertPosition);
    } else {
        if (existingRows.length > 0) {
            clonedRow.classList.add('border-b', 'border-gray-300');
            const currentLastRow = existingRows[existingRows.length - 1];
            currentLastRow.classList.add('border-b', 'border-gray-300');
        }
        availableCoursesDiv.appendChild(clonedRow);
    }

    // console.log('Course removed:', courseId);
    // console.log('Selected courses:', selectedCourses);
}

// Slot selection functionality
document.querySelectorAll('.bg-cyan-300').forEach(slot => {
    slot.addEventListener('click', function() {
        document.querySelectorAll('.bg-green-400, .bg-cyan-300').forEach(s => {
            s.className = s.className.replace('bg-green-400', 'bg-cyan-300');
            s.className = s.className.replace('text-white', 'text-gray-800');
        });

        this.className = this.className.replace('bg-cyan-300', 'bg-green-400');
        this.className = this.className.replace('text-gray-800', 'text-white');
    });
});

// Registration Form 3