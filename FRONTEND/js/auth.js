document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");
    const toggleText = document.getElementById("toggle-form");
    const formTitle = document.getElementById("form-title");
    const loginMessage = document.getElementById("login-message");
    const signupMessage = document.getElementById("signup-message");

    const BASE_URL = "http://localhost:8080"; 

    // change between login and signup
    toggleText.addEventListener("click", function (e) {
        e.preventDefault();
        if (signupForm.classList.contains("hidden")) {
            signupForm.classList.remove("hidden");
            loginForm.classList.add("hidden");
            formTitle.innerText = "Register";
            toggleText.innerHTML = `Already have an account? <a href="#">Login</a>`;
        } else {
            signupForm.classList.add("hidden");
            loginForm.classList.remove("hidden");
            formTitle.innerText = "Login";
            toggleText.innerHTML = `Don't have an account? <a href="#">Sign up</a>`;
        }
    });

    //  Login logic
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();
        loginMessage.innerText = ""; // Clear previous messages

        const loginData = {
            email: document.getElementById("login-email").value,
            password: document.getElementById("login-password").value
        };

        fetch(`${BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid credentials");
            }
            return response.json();
        })
        .then(data => {
            sessionStorage.setItem("employeeId", data.employeeId);
            sessionStorage.setItem("employeeName", data.name);
            sessionStorage.setItem("isAdmin", data.isAdmin);
            loginMessage.innerHTML = `<span style="color: green;">Login successful! Redirecting...</span>`;
            
            setTimeout(() => {
                if(data.isAdmin){
                window.location.href = "admin-dashboard.html";
                }
                else{
                window.location.href = "employee-dashboard.html";
                }
            }, 2000);
        })
        .catch(error => {
            loginMessage.innerHTML = `<span style="color: red;">${error.message}</span>`;
        });
    });

    // Registration logic 
    signupForm.addEventListener("submit", function (event) {
        event.preventDefault();
        signupMessage.innerText = "";

        const signupData = {
            name: document.getElementById("signup-name").value,
            email: document.getElementById("signup-email").value,
            password: document.getElementById("signup-password").value,
            department: document.getElementById("signup-department").value
        };

        fetch(`${BASE_URL}/auth/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(signupData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Registration failed. Email might already be in use.");
            }
            return response.json();
        })
        .then(data => {
            signupMessage.innerHTML = `<span style="color: green;">${data.message} Redirecting to login...</span>`;
            
            setTimeout(() => {
                formTitle.innerText = "Login";
                signupForm.classList.add("hidden");
                loginForm.classList.remove("hidden");
                signupMessage.innerText = "";
            }, 2000);
        })
        .catch(error => {
            signupMessage.innerHTML = `<span style="color: red;">${error.message}</span>`;
        });
    });
});
