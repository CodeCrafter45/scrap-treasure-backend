window.onload = function () {
    loadAvailableRequests();
};

function loadAvailableRequests() {
    fetch("http://localhost:8080/api/collector/requests", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        let requests = response.data || [];
        let html = "";

        if (requests.length === 0) {
            html = "<p>No available requests</p>";
        } else {
            requests.forEach(req => {

    console.log("REQ DATA:", req); // DEBUG

    const id = req.requestId || req.id; 

    let actionUI = "";

    if (req.status === "REQUESTED") {
        actionUI = `
            <button class="btn-accept" onclick="acceptRequest(${id})">
                Accept Job
            </button>
        `;
    } 
    else if (req.status === "ACCEPTED") {
        actionUI = `
            <div class="complete-box" style="margin-top: 10px; display: flex; gap: 5px;">
                <input type="number" id="weight-${id}" placeholder="Weight (kg)" style="width: 100px; padding: 5px;">
                <button class="btn-complete" onclick="completePickup(${id})">
                    Complete Pickup
                </button>
            </div>
        `;
    } 
    else {
        actionUI = `<span style="color: green;">✔ Completed</span>`;
    }

    html += `
      <div id="card-${req.requestId}" style="border: 1px solid #ddd; padding: 15px; margin-bottom: 10px; border-radius: 8px; background: #fff;">
            <p><b>Request ID:</b> #${id}</p>
            <p><b>Address:</b> ${req.address || "N/A"}</p>
            <p><b>Status:</b> ${req.status}</p>
            ${actionUI}
        </div>
    `;
});
        }

        document.getElementById("requestList").innerHTML = html;
    })
    .catch(err => console.error("Load Error:", err));
}

// ACCEPT REQUEST
function acceptRequest(id) {
    fetch(`http://localhost:8080/api/collector/requests/${id}/accept`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(() => {
        showToast("✅ Job accepted successfully! if your are verified collector", "#2e7d32");
        loadAvailableRequests();
    })
    .catch(err => {
        console.error(err);
        showToast("❌ Something went wrong!", "#d32f2f");
    });
}

// COMPLETE PICKUP
function completePickup(id) {
    const weightInput = document.getElementById(`weight-${id}`).value;
    const weight = parseFloat(weightInput);

    if (!weight || weight <= 0) {
        alert("Enter valid weight");
        return;
    }

    const price = weight * 20;

    
    let completed = JSON.parse(localStorage.getItem("completed") || "{}");
    completed[id] = { weight, price };
    localStorage.setItem("completed", JSON.stringify(completed));

    alert("Pickup Completed!");

    const card = document.getElementById(`card-${id}`);
    card.innerHTML = `
        <p><b>Request ID:</b> #${id}</p>
        <p><b>Status:</b> <span style="color: green;">COMPLETED</span></p>
        <p><b>Weight:</b> ${weight} kg</p>
        <p><b>Price:</b> ₹${price}</p>
    `;
}

function showToast(message, color = "#323232") {
    const toast = document.getElementById("toast");
    toast.innerText = message;
    toast.style.background = color;

    toast.style.opacity = "1";
    toast.style.transform = "translateY(0)";

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(20px)";
    }, 2500);
}