document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080';
    const urlParams = new URLSearchParams(window.location.search);
    const surveyId = urlParams.get('surveyId');
    const employeeId = urlParams.get('employeeId');

    const responsesContainer = document.getElementById('responsesContainer');

    // survey response by the emp
    fetch(`${BASE_URL}/responses/survey/${surveyId}/employee/${employeeId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch survey responses');
            }
            return response.json();
        })
        .then(responses => {
            if (responses.length === 0) {
                responsesContainer.innerHTML = `
                    <div class="no-responses">
                        <p>No responses found for this survey!</p>
                    </div>
                `;
                return;
            }

            
            responses.forEach(response => {
                const responseBox = document.createElement('div');
                responseBox.className = 'response-item';
                responseBox.innerHTML = `
                    <h2>Question</h2>
                    <p>${response.questionText}</p>
                    <h2>Your Response</h2>
                    <p>${response.response}</p>
                `;
                responsesContainer.appendChild(responseBox);
            });
        })
        .catch(error => {
            responsesContainer.innerHTML = `
                <div class="error-message">
                    <p>An error occurred while fetching survey responses. Please try again later.</p>
                </div>
            `;
            console.error('Error fetching survey responses:', error);
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