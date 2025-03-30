document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080'; 
    const employeeId = sessionStorage.getItem('employeeId'); 

    const activeSurveysContainer = document.getElementById('activeSurveysContainer');
    const responseArchivesContainer = document.getElementById('responseArchivesContainer');

    if (!employeeId) {
        alert('Employee ID not found. Please log in.');
        window.location.href = 'auth.html'; // Redirect {login check)}
        return;
    }

    //logout button logic
    const logoutButton = document.getElementById('logoutButton');
    logoutButton.addEventListener('click', () => {
        localStorage.clear(); 
        sessionStorage.clear();
        alert('You have logged out.');
        window.location.href = 'auth.html'; 
    });


    // Active Surveys
    fetch(`${BASE_URL}/surveys`)
        .then(response => response.json())
        .then(surveys => {
            if (surveys.length === 0) {
                activeSurveysContainer.innerHTML = `<p>No active surveys available!</p>`;
                return;
            }
            surveys.forEach(survey => {
                const surveyBox = document.createElement('div');
                surveyBox.className = 'survey-box';
                surveyBox.innerHTML = `
                    <h3>${survey.title}</h3>
                    <p>${survey.description}</p>
                    <button class="participate-btn" data-id="${survey.id}">Participate</button>
                `;
                surveyBox.querySelector('.participate-btn').addEventListener('click', () => {
                    window.location.href = `submit-response.html?surveyId=${survey.id}`;
                });
                activeSurveysContainer.appendChild(surveyBox);
            });
        })
        .catch(error => {
            activeSurveysContainer.innerHTML = `<p style="color: red;">Error: ${error.message}</p>`;
        });
        

    fetch(`${BASE_URL}/responses/employee/${employeeId}`)
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.text(); //  response as text resolved an error idk.
    })
    .then(text => {
        if (!text) {
           //no response
            responseArchivesContainer.innerHTML = `<p>No responses found!</p>`;
            return;
        }
        const archives = JSON.parse(text); 
        if (archives.length === 0) {
            responseArchivesContainer.innerHTML = `<p>No responses found!</p>`;
            return;
        }
        archives.forEach(archive => {
            const responseBox = document.createElement('div');
            responseBox.className = 'response-box';
            responseBox.innerHTML = `
                <h3>${archive.surveyTitle}</h3>
                <p>${archive.surveyDescription}</p>
                <button class="view-response-btn" data-survey-id="${archive.surveyId}">View Response</button>
                <button class="delete-survey-responses-btn" data-survey-id="${archive.surveyId}">Delete All Responses</button>
            `;

            responseBox.querySelector('.view-response-btn').addEventListener('click', () => {
                window.location.href = `response.html?surveyId=${archive.surveyId}&employeeId=${employeeId}`;
            });
        
            // Delete response logixc
            responseBox.querySelector('.delete-survey-responses-btn').addEventListener('click', () => {
                if (confirm('Are you sure you want to delete all responses for this survey?')) {
                    fetch(`${BASE_URL}/responses/survey/${archive.surveyId}/employee/${employeeId}`, { method: 'DELETE' })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error(`Failed to delete responses. Status: ${response.status}`);
                            }
                            alert('All responses for this survey have been deleted!');
                            responseBox.remove(); //ui update
                        })
                        .catch(error => {
                            alert(`Error: ${error.message}`);
                        });
                }
            });
        
            responseArchivesContainer.appendChild(responseBox);
        });
        
    })
    .catch(error => {
        responseArchivesContainer.innerHTML = `<p style="color: red;">Error: ${error.message}</p>`;
    });


});