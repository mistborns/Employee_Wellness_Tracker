document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080'; 
    const employeeTable = document.getElementById('employeeTable');
    const employeeId = sessionStorage.getItem('employeeId');
    const admin = sessionStorage.getItem('isAdmin');
    if (!employeeId || !admin) {
        alert('Employee ID not found. Please log in.');
        window.location.href = 'auth.html'; // Redirect {login check)}
        return;
    }

    //  Employee List
    fetch(`${BASE_URL}/employees`)
        .then(response => response.json())
        .then(employees => {
            employees.forEach(employee => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${employee.name}</td>
                    <td>${employee.email}</td>
                    <td>${employee.department}</td>
                    <td>
                        <button class="btn btn-edit" data-id="${employee.id}">Edit</button>
                        <button class="btn btn-delete" data-id="${employee.id}">Delete</button>
                        ${!employee.isAdmin ? `<button class="btn btn-promote" data-id="${employee.id}">Promote</button>` : ''}
                    </td>
                `;
                employeeTable.appendChild(row);

            
                const editButton = row.querySelector('.btn-edit');
                const deleteButton = row.querySelector('.btn-delete');
                const promoteButton = row.querySelector('.btn-promote');

                // Edit Employee
                editButton.addEventListener('click', () => {
                    editEmployee(employee);
                });

                // Delete Employee
                deleteButton.addEventListener('click', () => {
                    deleteEmployee(employee.id, row);
                });

                // Promote Employee (Only if not  admin)
                if (promoteButton) {
                    promoteButton.addEventListener('click', () => {
                        promoteEmployee(employee.id, row);
                    });
                }
            });
        })
        .catch(error => console.error('Error fetching employees:', error));

    // edit employee logic
    const modal = document.getElementById('editEmployeeModal');
    const closeModal = document.querySelector('.close');
    const saveButton = document.getElementById('saveEdit');
    let currentEmployee = null; // Store the full employee object
    
    // Open the modal with employee details
    const editEmployee = (employee) => {
        currentEmployee = employee; // Store the whole object
    
        document.getElementById('editName').value = employee.name || '';
        document.getElementById('editEmail').value = employee.email || '';
        document.getElementById('editDepartment').value = employee.department || '';
    
        modal.classList.add('show'); 
    };
    
    //modal
    closeModal.addEventListener('click', () => {
        modal.classList.remove('show'); 
    });
    
    // Saving the  employee updates
    saveButton.addEventListener('click', () => {
        if (!currentEmployee) return; 
    
        const updatedEmployee = {
            name: document.getElementById('editName').value || null,
            email: document.getElementById('editEmail').value || null,
            department: document.getElementById('editDepartment').value || null
        };
    
        fetch(`${BASE_URL}/employees/${currentEmployee.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedEmployee),
        })
        .then(response => {
            if (!response.ok) throw new Error('Failed to update employee');
            return response.json();
        })
        .then(() => {
            alert('Employee updated successfully!');
            modal.classList.remove('show');
            location.reload();
        })
        .catch(error => console.error('Error updating employee:', error));
    });

    // delete the emp
    const deleteEmployee = (id, rowElement) => {
        if (confirm('Are you sure you want to delete this employee?')) {
            fetch(`${BASE_URL}/employees/${id}`, { method: 'DELETE' })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to delete employee');
                    }
                    alert('Employee deleted successfully!');
                    rowElement.remove(); 
                })
                .catch(error => console.error('Error deleting employee:', error));
        }
    };

    // promotes the emp
    const promoteEmployee = (id, rowElement) => {
        if (confirm('Are you sure you want to promote this employee to admin?')) {
            fetch(`${BASE_URL}/employees/${id}/promote`, {
                method: 'PUT',
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to promote employee');
                    }
                    alert('Employee promoted to admin successfully!');
                    location.reload(); 
                })
                .catch(error => console.error('Error promoting employee:', error));
        }
    };
    
    //modal
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.classList.remove('show');
        }
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