

function loadCollectors() {
    fetch("http://localhost:8080/api/admin/collectors", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        let collectors = response.data;

        let html = "";

        collectors.forEach(c => {
            html += `
                <div style="border:1px solid #ccc; margin:10px; padding:10px;">
                    <p><b>Email:</b> ${c.email}</p>
                    <p><b>Verified:</b> ${c.enabled}</p>

                    ${
                        !c.enabled
                        ? `<button onclick="verifyCollector(${c.id})">Verify</button>`
                        : `<span>Verified ✅</span>`
                    }
                </div>
            `;
        });

        document.getElementById("collectorList").innerHTML = html;
    });
}

function verifyCollector(id) {
    fetch(`http://localhost:8080/api/admin/collectors/${id}/verify`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(() => {
        alert("Collector Verified!");
        loadCollectors();
    });
}

function loadCategories() {
    fetch("http://localhost:8080/api/admin/scrap-categories", {
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
    .then(res => res.json())
    .then(response => {
        let categories = response.data;

        let html = "";

        categories.forEach(cat => {
            html += `
                <div style="border:1px solid #ccc; margin:10px; padding:10px;">
                    <p><b>Name:</b> ${cat.name}</p>
                    <p><b>Price:</b> ${cat.pricePerKg}</p>
                    <p><b>Status:</b> ${cat.active}</p>

                </div>
            `;
        });

        document.getElementById("categoryList").innerHTML = html;
    });
}

function addCategory() {
    const name = document.getElementById("catName").value;
    const price = document.getElementById("catPrice").value;

    fetch("http://localhost:8080/api/admin/scrap-categories", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({
            name: name,
            pricePerKg: price
        })
    })
    .then(() => {
        alert("Category Added!");
        loadCategories();
    });
}

function addCategory() {
    const name = document.getElementById("catName").value;
    const price = document.getElementById("catPrice").value;

    fetch("http://localhost:8080/api/admin/scrap-categories", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({
            name: name,
            pricePerKg: price
        })
    })
    .then(() => {
        alert("Category Added!");
        loadCategories();
    });
}

