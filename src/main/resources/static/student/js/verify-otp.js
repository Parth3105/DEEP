// Toast Notification
function showToast(message, type = 'error') {
    const toast = document.getElementById("toast-error");
    const text = document.getElementById("toast-message");

    text.innerText = message;

    // Reset any previous background color
    toast.classList.remove("bg-red-600", "bg-yellow-400");

    // Apply based on type
    if (type === 'error') {
        toast.classList.add("bg-red-600");
    } else if (type === 'warning') {
        toast.classList.add("bg-yellow-400", "text-gray-900");
    }

    toast.classList.remove("hidden");
    toast.classList.add("flex");

    setTimeout(() => {
        hideToast();
    }, 5000);
}

function hideToast() {
    const toast = document.getElementById("toast-error");
    toast.classList.remove("flex");
    toast.classList.add("hidden");
}

document.addEventListener("DOMContentLoaded", function () {
    let timeLeft = 2 * 60; // 2 minutes
    const countdownEl = document.getElementById("countdown");
    const timerMessage = document.getElementById("timerMessage");
    const resendLink = document.getElementById("resendLink");

    const timer = setInterval(() => {
      const minutes = Math.floor(timeLeft / 60);
      const seconds = timeLeft % 60;
      countdownEl.textContent = `${minutes}:${seconds < 10 ? '0' + seconds : seconds}`;

      if (timeLeft <= 0) {
        clearInterval(timer);

        // Remove timer message
        timerMessage.remove();

        // Enable resend link
        resendLink.classList.remove("text-blue-400", "cursor-not-allowed", "pointer-events-none");
        resendLink.classList.add("text-blue-600", "hover:text-blue-700", "cursor-pointer");
        resendLink.removeAttribute("style");
      }

      timeLeft--;
    }, 1000);
});

function generateToken() {
    return Math.random().toString(36).substring(2, 15);
}

function handleResendClick(event) {
    event.preventDefault();
    const token = generateToken();

    // Set token in hidden input
    document.getElementById("resendToken").value = token;
    console.log(token);

    // Set the form action with token in URL
    const form = document.getElementById("resendForm");
    form.action = `/resend-otp/${token}`;
    form.submit();
}

const otpInputs = document.querySelectorAll('.otp-input');
const combinedOtpInput = document.getElementById('combined_otp');

// Function to update the combined OTP value
function updateCombinedOtp() {
    let otp = '';
    otpInputs.forEach(input => {
        otp += input.value;
    });
    combinedOtpInput.value = otp;
}

// Add event listeners to each OTP input
otpInputs.forEach((input, index) => {
    input.addEventListener('input', function(e) {
        const value = e.target.value;

        // Only allow numbers
        if (!/^\d*$/.test(value)) {
            e.target.value = '';
            return;
        }

        // Move to next input if current is filled
        if (value && index < otpInputs.length - 1) {
            otpInputs[index + 1].focus();
        }

        updateCombinedOtp();
    });

    input.addEventListener('keydown', function(e) {
        // Handle backspace - move to previous input if current is empty
        if (e.key === 'Backspace' && !e.target.value && index > 0) {
            otpInputs[index - 1].focus();
        }

        // Handle arrow keys for navigation
        if (e.key === 'ArrowLeft' && index > 0) {
            otpInputs[index - 1].focus();
        }
        if (e.key === 'ArrowRight' && index < otpInputs.length - 1) {
            otpInputs[index + 1].focus();
        }
    });

    input.addEventListener('paste', function(e) {
        e.preventDefault();
        const pastedData = e.clipboardData.getData('text/plain');

        // Only process if pasted data contains only digits
        if (!/^\d+$/.test(pastedData)) {
            return;
        }

        // Fill inputs with pasted digits
        for (let i = 0; i < Math.min(pastedData.length, otpInputs.length - index); i++) {
            otpInputs[index + i].value = pastedData[i];
        }

        updateCombinedOtp();

        // Focus on the next empty input or the last input
        const nextEmptyIndex = Math.min(index + pastedData.length, otpInputs.length - 1);
        otpInputs[nextEmptyIndex].focus();
    });
});

// Prevent form submission if OTP is not complete
document.querySelector('form').addEventListener('submit', function(e) {
    updateCombinedOtp();
    if (combinedOtpInput.value.length !== 6) {
        e.preventDefault();
        showToast('Please enter complete 6-digit OTP');
    }
});