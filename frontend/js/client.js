function createRequest() {
    console.log("Button clicked");

    const data = {
        categoryId: document.getElementById("categoryId").value,
        address: document.getElementById("address").value
    };

    console.log(data);

    fetch("http://localhost:8080/api/client/requests", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(res => {
        console.log(res);
        alert("Request Created");
        loadRequests(); // reload list
    })
    .catch(err => console.error(err));
}


function loadRequests() {
    fetch("http://localhost:8080/api/client/requests", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        console.log("API Response:", response);

        let requests = response.data; // 🔥 MAIN FIX

        let html = "";

        if (!requests || requests.length === 0) {
            html = "<p>No requests found</p>";
        } else {
            requests.forEach(req => {
                html += `
                    <div style="border:1px solid #ccc; padding:10px; margin:10px;">
                        <p><b>ID:</b> ${req.requestId}</p>
                        <p><b>Status:</b> ${req.status}</p>
                        <p><b>Address:</b> ${req.address}</p>
                        <p><b>Weight:</b> ${req.weightKg || "Not collected"}</p>
                        <p><b>Price:</b> ${req.price || "Pending"}</p>
                    </div>
                `;
            });
        }

        document.getElementById("requestList").innerHTML = html;
    })
    .catch(err => console.error(err));
}