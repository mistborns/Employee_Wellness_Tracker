document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080'; 
    const employeeId = sessionStorage.getItem('employeeId'); 
    const isAdmin = sessionStorage.getItem('isAdmin');
    
    const updateDetailsButton = document.getElementById('updateDetailsButton');
    const changePasswordButton = document.getElementById('changePasswordButton');
    const updateDetailsModal = document.getElementById('updateDetailsModal');
    const closeUpdateDetailsModal = document.getElementById('closeUpdateDetailsModal');
    const changePasswordModal = document.getElementById('changePasswordModal');
    const closeChangePasswordModal = document.getElementById('closeChangePasswordModal');
    const updateDetailsForm = document.getElementById('updateDetailsForm');
    const changePasswordForm = document.getElementById('changePasswordForm');
    const backBtn = document.getElementById('backToDashboardButton');

    backBtn.addEventListener("click" , () => {
        if(isAdmin === "true"){
            window.location.href = "admin-dashboard.html";
        }
        else{
            window.location.href = "employee-dashboard.html";
        }
    })

    //  Profile Detail
    fetch(`${BASE_URL}/employees/${employeeId}`) 
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch employee profile');
            }
            return response.json();
        })
        .then(profile => {
            document.getElementById('employeeName').innerText = profile.name || 'N/A';
            document.getElementById('employeeEmail').innerText = profile.email || 'N/A';
            document.getElementById('employeeDepartment').innerText = profile.department || 'N/A';
        })
        .catch(error => console.error('Error fetching profile:', error));

    // modal logic
    updateDetailsButton.addEventListener('click', () => {
        updateDetailsModal.style.display = 'flex';
    });

    closeUpdateDetailsModal.addEventListener('click', () => {
        updateDetailsModal.style.display = 'none';
    });

    changePasswordButton.addEventListener('click', () => {
        changePasswordModal.style.display = 'flex';
    });

    closeChangePasswordModal.addEventListener('click', () => {
        changePasswordModal.style.display = 'none';
    });

    window.addEventListener('click', (event) => {
        if (event.target === updateDetailsModal) {
            updateDetailsModal.style.display = 'none';
        }
        if (event.target === changePasswordModal) {
            changePasswordModal.style.display = 'none';
        }
    });

    // profile update
    updateDetailsForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const updatedDetails = {
            name: document.getElementById('updateName').value || undefined,
            email: document.getElementById('updateEmail').value || undefined,
            department: document.getElementById('updateDepartment').value || undefined,
        };

        fetch(`${BASE_URL}/employees/${employeeId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedDetails),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update profile');
                }
                return response.json();
            })
            .then(data => {
                alert('Profile updated successfully!');
                location.reload(); 
            })
            .catch(error => {
                console.error('Error updating profile:', error);
                alert('Failed to update profile. Please try again.');
            });
    });

    // password change
    changePasswordForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (newPassword !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        const passwordDetails = {
            password: newPassword,
        };

        fetch(`${BASE_URL}/employees/${employeeId}`, { 
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(passwordDetails),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update password');
                }
                alert('Password updated successfully!');
                changePasswordModal.style.display = 'none';
            })
            .catch(error => {
                console.error('Error updating password:', error);
                alert('Failed to update password. Please try again.');
            });
    });

    // Logout 
    const logoutButton = document.getElementById('logoutButton');
    logoutButton.addEventListener('click', () => {
        if (confirm('Are you sure you want to logout?')) {
            sessionStorage.clear(); 
            window.location.href = 'auth.html'; 
        }
    });
});