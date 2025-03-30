document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080';
    const surveyId = new URLSearchParams(window.location.search).get('surveyId');
    const employeeId = sessionStorage.getItem('employeeId'); 
    const surveyTitleElement = document.getElementById('surveyTitle');
    const surveyDescriptionElement = document.getElementById('surveyDescription');
    const questionContainer = document.getElementById('questionContainer');
    const responseForm = document.getElementById('responseForm');

    // survey  and questions
    fetch(`${BASE_URL}/surveys/${surveyId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch survey details');
            }
            return response.json();
        })
        .then(survey => {
            surveyTitleElement.innerText = survey.title;
            surveyDescriptionElement.innerText = survey.description;

           //this adds questions
            survey.questions.forEach(question => {
                const questionBox = document.createElement('div');
                questionBox.className = 'question-box';
                questionBox.innerHTML = `
                    <h3>${question.text}</h3>
                    <input type="text" name="question-${question.id}" class="response-input" placeholder="Enter your response" required>
                `;
                questionContainer.appendChild(questionBox);
            });
        })
        .catch(error => console.error('Error fetching survey details:', error));

    // submission logic
    responseForm.addEventListener('submit', event => {
        event.preventDefault();

        // this prevents duplicate submission by emp
        fetch(`${BASE_URL}/responses/survey/${surveyId}/employee/${employeeId}`)
            .then(response => {
                if (!response.ok) {
                    return Promise.resolve([]); 
                }
                return response.text(); 
            })
            .then(text => {
                return text ? JSON.parse(text) : []; 
            })
            .then(existingResponses => {
                if (existingResponses.length > 0) {
                    alert('You have already submitted this survey!');
                    return;
                }

                
                const responseBatchDTO = {
                    employeeId: parseInt(employeeId),
                    surveyId: parseInt(surveyId),
                    responseDTOList: []
                };

                // making the response format that the backendapi expects
                const responseInputs = document.querySelectorAll('.response-input');
                responseInputs.forEach(input => {
                    const questionId = input.name.split('-')[1]; 
                    const responseText = input.value.trim(); 

                    if (responseText) {
                        responseBatchDTO.responseDTOList.push({
                            questionId: parseInt(questionId),
                            response: responseText
                        });
                    }
                });

                // Submit responses
                fetch(`${BASE_URL}/responses/${surveyId}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(responseBatchDTO)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Failed to submit responses');
                        }
                        return response.json();
                    })
                    .then(data => {
                        alert('Responses submitted successfully!');
                        window.location.href = 'employee-dashboard.html'; 
                    })
                    .catch(error => {
                        alert(`Error: ${error.message}`);
                    });
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