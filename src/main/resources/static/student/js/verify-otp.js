if(otpVerificationResponse) {
    printStatusResponse(otpVerificationResponse);
}

document.addEventListener("DOMContentLoaded", function () {
  const STORAGE_KEY = "otpCountdownExpiry";
  localStorage.removeItem(STORAGE_KEY);

  const resendLink = document.getElementById("resendLink");
  const timerContainer = document.getElementById("timerContainer");
  const TIMER_DURATION = 2 * 60; // 2 minutes

  function insertTimerMessage() {
    timerContainer.innerHTML = `
      <div id="timerMessage" class="text-gray-500 mb-2">
        You can request to resend the OTP after
        <span id="countdown" class="font-semibold text-blue-600">2:00</span>
      </div>`;
  }

  function updateCountdownDisplay(timeLeft) {
    const countdownEl = document.getElementById("countdown");
    if (countdownEl) {
      const minutes = Math.floor(timeLeft / 60);
      const seconds = timeLeft % 60;
      countdownEl.textContent = `${minutes}:${seconds < 10 ? '0' + seconds : seconds}`;
    }
  }

  function showResendLink() {
    timerContainer.innerHTML = ""; // remove timer
    resendLink.style.display = "inline-block";
    resendLink.textContent = "Resend OTP";
    resendLink.classList.remove("text-gray-500", "cursor-not-allowed", "pointer-events-none");
    resendLink.classList.add("text-blue-600", "hover:text-blue-700", "cursor-pointer");
  }

  function startTimer(duration) {
    const expiryTime = Date.now() + duration * 1000;
    localStorage.setItem(STORAGE_KEY, expiryTime.toString());

    insertTimerMessage(); // restore UI
    let timeLeft = duration;
    updateCountdownDisplay(timeLeft);

    const timer = setInterval(() => {
      timeLeft--;
      updateCountdownDisplay(timeLeft);

      if (timeLeft <= 0) {
        clearInterval(timer);
        localStorage.removeItem(STORAGE_KEY);
        showResendLink();
      }
    }, 1000);
  }

  function resumeTimer() {
    const expiry = localStorage.getItem(STORAGE_KEY);
    if (expiry) {
      const remaining = Math.floor((parseInt(expiry) - Date.now()) / 1000);
      if (remaining > 0) {
        resendLink.style.display = "none";
        startTimer(remaining);
      } else {
        showResendLink();
      }
    } else {
      resendLink.style.display = "none";
      startTimer(TIMER_DURATION);
    }
  }

  // Resend handler — triggered on form submit
  window.handleResendClick = function (event) {
    resendLink.onclick = null;

    resendLink.textContent = "Resend request sent";
    resendLink.classList.remove("hover:text-blue-700", "cursor-pointer");
    resendLink.classList.add("text-gray-500", "cursor-not-allowed", "pointer-events-none");

    const form = document.getElementById("resendForm");
    form.submit();

    setTimeout(() => {
      resendLink.style.display = "none";
      startTimer(TIMER_DURATION);
    }, 10000);
  };

  resumeTimer();
});

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