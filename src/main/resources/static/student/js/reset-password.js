function togglePasswordVisibility() {
  const input = document.getElementById('floating_password');
  const icon = document.getElementById('passwordToggleIcon');

  const isPassword = input.type === 'password';
  input.type = isPassword ? 'text' : 'password';
  icon.src = isPassword ? '/student/images/view.svg' : '/student/images/close-eye.svg';
  icon.alt = isPassword ? 'Hide password' : 'Show password';
}

function toggleConfirmPasswordVisibility() {
  const input = document.getElementById('floating_confirm_password');
  const icon = document.getElementById('confirmPasswordToggleIcon');

  const isPassword = input.type === 'password';
  input.type = isPassword ? 'text' : 'password';
  icon.src = isPassword ? '/student/images/view.svg' : '/student/images/close-eye.svg';
  icon.alt = isPassword ? 'Hide password' : 'Show password';
}