async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch(BASE_URL + "/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (data.token) { 
          
            const token = data.token;
            const role = data.role;  

         
            localStorage.setItem("token", token);
            localStorage.setItem("role", role);

            alert("Login successful!");

          
            if (role === "CLIENT") {
                window.location.href = "client.html";
            } else if (role === "COLLECTOR") {
                window.location.href = "collector.html";
            } else if (role === "ADMIN") {
                window.location.href = "admin.html";
            }
        } else {
            document.getElementById("error").innerText = "Invalid email or password.";
        }

    } catch (error) {
        console.error(error); 
        document.getElementById("error").innerText = "Server error";
    }
}
//