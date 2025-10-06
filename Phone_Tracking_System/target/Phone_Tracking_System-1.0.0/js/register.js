document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const strengthIndicator = document.getElementById('passwordStrength');
    const matchIndicator = document.getElementById('passwordMatch');
    
    // Password strength checker
    if (passwordInput && strengthIndicator) {
        passwordInput.addEventListener('input', function() {
            const password = this.value;
            const strength = checkPasswordStrength(password);
            
            strengthIndicator.className = 'password-strength';
            
            if (password.length > 0) {
                if (strength.score <= 2) {
                    strengthIndicator.classList.add('weak');
                } else if (strength.score <= 3) {
                    strengthIndicator.classList.add('medium');
                } else {
                    strengthIndicator.classList.add('strong');
                }
            }
        });
    }
    
    // Password match checker
    if (confirmPasswordInput && matchIndicator) {
        confirmPasswordInput.addEventListener('input', function() {
            const password = passwordInput.value;
            const confirmPassword = this.value;
            
            matchIndicator.className = 'password-match';
            
            if (confirmPassword.length > 0) {
                if (password === confirmPassword) {
                    matchIndicator.classList.add('match');
                    matchIndicator.textContent = '✓ Passwords match';
                } else {
                    matchIndicator.classList.add('no-match');
                    matchIndicator.textContent = '✗ Passwords do not match';
                }
            } else {
                matchIndicator.textContent = '';
            }
        });
    }
    
    // Form validation
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;
            
            if (password !== confirmPassword) {
                event.preventDefault();
                alert('Passwords do not match!');
                return false;
            }
            
            if (checkPasswordStrength(password).score < 3) {
                event.preventDefault();
                alert('Password is too weak! Please choose a stronger password.');
                return false;
            }
        });
    }
});

function checkPasswordStrength(password) {
    let score = 0;
    const checks = {
        length: password.length >= 8,
        lowercase: /[a-z]/.test(password),
        uppercase: /[A-Z]/.test(password),
        numbers: /\d/.test(password),
        symbols: /[^A-Za-z0-9]/.test(password)
    };
    
    Object.values(checks).forEach(check => {
        if (check) score++;
    });
    
    return {
        score: score,
        checks: checks
    };
}