window.onload = function () {
    loadCategories();
    loadRequests();
};

function loadRequests() {
    fetch("http://localhost:8080/api/client/requests", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        let requests = response.data || [];
        let html = "";

        if (requests.length === 0) {
            html = "<p>You haven't made any requests yet.</p>";
        } else {
           requests.forEach(req => {

    const id = req.requestId || req.id;

    let completed = JSON.parse(localStorage.getItem("completed") || "{}");

   
    let statusColor = "#f39c12";
    if (req.status === "COMPLETED") statusColor = "#27ae60";
    if (req.status === "REQUESTED") statusColor = "#3498db";

  let weightDisplay;
let priceDisplay;


if (completed[id]) {
    weightDisplay = `<b style="color:#2e7d32;">${completed[id].weight} kg</b>`;
    priceDisplay = `<b style="color:#1b5e20;">₹${completed[id].price}</b>`;
    statusColor = "#27ae60"; // completed
} 
else {
    let weight = `<span style="color:#757575;">⏳ Pickup pending</span>`;
    let price = `<span style="color:#9e9e9e;">Will be calculated after collection</span>`;

    if (req.status && req.status.toUpperCase() === "ACCEPTED") {
        weight = `<span style="color:#1565c0; font-weight:600;">🚚 Pickup scheduled</span>`;
        price = `<span style="color:#616161;">Price will updated at client side</span>`;
    }

    if (req.status && req.status.toUpperCase() === "COMPLETED") {
        weight = `<b style="color:#2e7d32;">${req.weightKg || 0} kg</b>`;
        price = `<b style="color:#1b5e20;">₹${req.price || 0}</b>`;
    }

    weightDisplay = weight;
    priceDisplay = price;
}

    html += `
        <div style="border: 1px solid #eee; padding: 15px; margin-bottom: 15px; border-radius: 12px; background: #fff;">
            <p>ID: #${id}</p>
            <p><strong>Status:</strong> 
                <span style="color: ${completed[id] ? "#27ae60" : statusColor}">
                    ${completed[id] ? "COMPLETED" : req.status}
                </span>
            </p>
            <p><strong>Address:</strong> ${req.address}</p>

            <p><strong>Weight:</strong> ${weightDisplay}</p>
            <p><strong>Price:</strong> ${priceDisplay}</p>
        </div>
    `;
});
        }
        document.getElementById("requestList").innerHTML = html;
    })
    .catch(err => console.error("Client Load Error:", err));
}

function createRequest() {
    const categoryId = document.getElementById("category").value;
    const address = document.getElementById("address").value;

    if (!categoryId || !address) {
        alert("Please select a category and enter your address.");
        return;
    }

    const data = {
        categoryId: parseInt(categoryId),
        address: address,
        preferredTime: null
    };

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
        if (res.success) {
            alert("Request Posted Successfully!");
            document.getElementById("address").value = ""; // Clear input
            loadRequests();
        } else {
            alert("Error: " + res.message);
        }
    })
    .catch(err => console.error(err));
}

function loadCategories() {
    fetch("http://localhost:8080/api/public/scrap-categories", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        let categories = response.data || [];
        let html = "<option value=''>Select Category</option>";
        categories.forEach(cat => {
            html += `<option value="${cat.id}">${cat.name}</option>`;
        });
        document.getElementById("category").innerHTML = html;
    })
    .catch(err => console.error(err));
}