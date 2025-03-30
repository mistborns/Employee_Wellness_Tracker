document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080'; 
    const surveyTable = document.getElementById('surveyTable');

    // display surveys
    fetch(`${BASE_URL}/surveys`)
        .then(response => response.json())
        .then(surveys => {
            surveys.forEach(survey => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${survey.title}</td>
                    <td>${survey.description}</td>
                    <td>${survey.isActive ? 'Active' : 'Inactive'}</td>
                    <td>
                        <button class="btn btn-edit" data-id="${survey.id}">Edit</button>
                        <button class="btn btn-delete" data-id="${survey.id}">Delete</button>
                    </td>
                `;
                surveyTable.appendChild(row);

                // listeners 
                const editButton = row.querySelector('.btn-edit');
                const deleteButton = row.querySelector('.btn-delete');

               
                editButton.addEventListener('click', () => {
                    editSurvey(survey);
                });

                
                deleteButton.addEventListener('click', () => {
                    deleteSurvey(survey.id, row);
                });
            });
        })
        .catch(error => console.error('Error fetching surveys:', error));

    // Edit the  Survey 
    const editSurvey = (survey) => {
        const updatedTitle = prompt('Enter new title:', survey.title);
        const updatedDescription = prompt('Enter new description:', survey.description);

        if (updatedTitle && updatedDescription) {
            const updatedSurvey = {
                title: updatedTitle,
                description: updatedDescription,
                isActive: survey.isActive, 
                questions: survey.questions 
            };

            fetch(`${BASE_URL}/surveys/${survey.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedSurvey),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to update survey');
                    }
                    return response.json();
                })
                .then(data => {
                    alert('Survey updated successfully!');
                    location.reload(); 
                })
                .catch(error => console.error('Error updating survey:', error));
        }
    };

    // Delete Survey
    const deleteSurvey = (id, rowElement) => {
        if (confirm('Are you sure you want to delete this survey?')) {
            fetch(`${BASE_URL}/surveys/${id}`, {
                method: 'DELETE',
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to delete survey');
                    }
                    alert('Survey deleted successfully!');
                    rowElement.remove();
                })
                .catch(error => console.error('Error deleting survey:', error));
        }
    };

    // Logout 
    const logoutButton = document.getElementById('logoutButton');
    logoutButton.addEventListener('click', () => {
        if (confirm('Are you sure you want to logout?')) {
            sessionStorage.clear(); 
            window.location.href = 'auth.html'; 
        }
    });
});