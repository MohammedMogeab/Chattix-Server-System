document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const response = await fetch('http://localhost:8087/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            username,
            password
        }),
        redirect: 'follow' // So it follows the redirect to /chat.html
    });

    if (response.redirected) {
        localStorage.setItem('username', username); // 👈 store for chat page
        window.location.href = response.url;

    } else {
        document.getElementById('message').innerText = 'Login failed. Try again.';
    }
});

