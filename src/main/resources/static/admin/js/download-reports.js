// Mobile Navbar Menu Toggle
const toggle = document.getElementById('menuToggle');
const menu = document.getElementById('menu');
toggle.addEventListener('click', () => {
  menu.classList.toggle('hidden');
});


//// Add interactivity for semester selection
//document.addEventListener('DOMContentLoaded', function() {
//    const semesterGroups = document.querySelectorAll('.flex.flex-wrap.gap-2');
//
//    semesterGroups.forEach(group => {
//        const buttons = group.querySelectorAll('button');
//
//        buttons.forEach(button => {
//            button.addEventListener('click', function() {
//                // Remove active state from all buttons in this group
//                buttons.forEach(btn => {
//                    btn.classList.remove('bg-[#2D9D5D]');
//                    btn.classList.add('bg-[#1E3C72]');
//                });
//
//                // Add active state to clicked button
//                this.classList.remove('bg-[#1E3C72]');
//                this.classList.add('bg-[#2D9D5D]');
//            });
//        });
//    });
//});

document.addEventListener('DOMContentLoaded', function() {
    let selectedSemester = null;

    // Get all semester buttons, download buttons and semester inputs
    const semesterBtns = document.querySelectorAll('.semester-btn');
    const downloadBtns = document.querySelectorAll('.download-btn');
    const semesterInputs = document.querySelectorAll('.semester-input');

    // Handle semester selection
    semesterBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            // Remove active state from all buttons
            semesterBtns.forEach(b => {
                b.style.backgroundColor = '#1E3C72';
            });

            // Set active state for clicked button
            this.style.backgroundColor = '#2D9D5D';
            selectedSemester = this.dataset.sem;

            // Update all hidden semester inputs
            semesterInputs.forEach(input => {
                input.value = selectedSemester;
            });

            // Enable download buttons
            downloadBtns.forEach(downloadBtn => {
                downloadBtn.disabled = false;
                downloadBtn.style.opacity = '1';
                downloadBtn.style.cursor = 'pointer';
            });
        });
    });

    // Initialize download buttons as disabled
    downloadBtns.forEach(btn => {
        btn.style.opacity = '0.6';
        btn.style.cursor = 'not-allowed';
    });

    // Add click handlers for visual feedback
    downloadBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            if (!selectedSemester) {
                e.preventDefault();
                alert('Please select a semester first!');
                return;
            }

            // Show loading state briefly
            const originalText = this.innerHTML;
            this.innerHTML = '<span class="animate-spin inline-block w-4 h-4 border-2 border-white border-t-transparent rounded-full mr-2"></span>Downloading...';

            // Reset button state after a short delay
            setTimeout(() => {
                this.innerHTML = originalText;
            }, 1500);
        });
    });
});