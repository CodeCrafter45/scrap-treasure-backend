function loadAvailableRequests() {
    fetch("http://localhost:8080/api/collector/requests", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        console.log("Collector API:", response);

        let requests = response.data;

        let html = "";

        if (!requests || requests.length === 0) {
            html = "<p>No available requests</p>";
        } else {
            requests.forEach(req => {
                html += `
                    <div style="border:1px solid #ccc; padding:10px; margin:10px;">
                        <p><b>ID:</b> ${req.requestId}</p>
                        <p><b>Address:</b> ${req.address}</p>
                        <p><b>Status:</b> ${req.status}</p>

                        ${
                            req.status === "REQUESTED"
                            ? `<button onclick="acceptRequest(${req.requestId})">Accept</button>`

                            : req.status === "ACCEPTED"
                            ? `
                                <input id="weight-${req.requestId}" placeholder="Enter weight (kg)">
                                <button onclick="completePickup(${req.requestId})">
                                    Complete Pickup
                                </button>
                              `

                            : `<p style="color:green;"><b>Completed ✅</b></p>`
                        }
                    </div>
                `;
            });
        }

        document.getElementById("requestList").innerHTML = html;
    })
    .catch(err => console.error(err));
}


function acceptRequest(id) {
    fetch(`http://localhost:8080/api/collector/requests/${id}/accept`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(() => {
        alert("Request Accepted!");
        loadAvailableRequests();
    })
    .catch(err => console.error(err));
}


function completePickup(id) {
    const weight = document.getElementById(`weight-${id}`).value;

    fetch(`http://localhost:8080/api/collector/pickup/${id}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({
            weightKg: weight
        })
    })
    .then(res => res.json())
    .then(() => {
        alert("Pickup Completed!");
        loadAvailableRequests();
    })
    .catch(err => console.error(err));
}