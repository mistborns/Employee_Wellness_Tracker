document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080'; 
    const createSurveyForm = document.getElementById('createSurveyForm');
    const questionsContainer = document.getElementById('questionsContainer');
    const addQuestionButton = document.getElementById('addQuestionButton');

    let questions = []; 

    // Add Question Logic
    addQuestionButton.addEventListener('click', () => {
        const questionId = questions.length;
        const questionElement = document.createElement('div');
        questionElement.className = 'question-item';
        questionElement.innerHTML = `
            <input type="text" class="form-control question-input" placeholder="Enter question text" data-id="${questionId}" required>
            <button type="button" class="btn btn-danger delete-question-btn" data-id="${questionId}">Delete</button>
        `;
        questionsContainer.appendChild(questionElement);

        // delete question button
        questionElement.querySelector('.delete-question-btn').addEventListener('click', () => {
            questionsContainer.removeChild(questionElement); 
            questions = questions.filter((_, index) => index !== questionId); 
        });

        questions.push({ text: '' });
        questionElement.querySelector('.question-input').addEventListener('input', (event) => {
            const id = event.target.dataset.id;
            questions[id].text = event.target.value; 
        });
    });

    // Submit Survey Logic
    createSurveyForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const surveyDetails = {
            title: document.getElementById('surveyTitle').value,
            description: document.getElementById('surveyDescription').value,
            isActive: true,
            questions: questions.filter(question => question.text.trim() !== ''), // Include only non-empty questions
        };

        fetch(`${BASE_URL}/surveys`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(surveyDetails),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to create survey');
                }
                return response.json();
            })
            .then(data => {
                alert('Survey created successfully!');
                //form reset
                document.getElementById('surveyTitle').value = '';
                document.getElementById('surveyDescription').value = '';
                questionsContainer.innerHTML = '';
                questions = [];
            })
            .catch(error => alert(`Error creating survey: ${error.message}`));
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