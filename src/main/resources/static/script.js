const BASE_URL = "http://localhost:8080/api";

// =======================
// Products
// =======================
function loadProducts(withOrderBtn = false) {
    fetch(`${BASE_URL}/products`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("products");
            if (!container) return;
            container.innerHTML = "";
            data.forEach(p => {
                const div = document.createElement("div");
                div.innerHTML = `
          <h3>${p.name}</h3>
          <p>${p.description}</p>
          <p><b>Price:</b> Rs.${p.price}</p>
          ${withOrderBtn ? `<button class="primary" onclick="placeOrder(${p.id})">🛒 Order Now</button>` : ""}
        `;
                container.appendChild(div);
            });
        });
}

function placeOrder(productId) {
    const order = {
        customerName: "Guest User",
        customerEmail: "guest@example.com",
        items: [{ product: { id: productId }, quantity: 1 }]
    };

    fetch(`${BASE_URL}/orders`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(order)
    })
        .then(res => res.json())
        .then(() => {
            window.location.href = "orders.html";
        });
}

// =======================
// User Orders
// =======================
function loadOrders(email) {
    fetch(`${BASE_URL}/orders/track?email=${email}`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("ordersList");
            if (!container) return;
            container.innerHTML = "";
            data.forEach(order => {
                const div = document.createElement("div");
                div.innerHTML = `
          <h3>Order #${order.id} <span style="color:#355CFF;">(${order.status})</span></h3>
          <ul>
            ${order.items.map(item => `<li>${item.product.name} (Qty: ${item.quantity})</li>`).join("")}
          </ul>
          ${order.status === "PENDING" ? `<button class="danger" onclick="deleteOrder(${order.id}, 'user')">❌ Cancel Order</button>` : ""}
          ${order.status === "DELIVERED" ? `
            <form onsubmit="submitFeedback(event, ${order.items[0].product.id})">
              <input type="text" id="comment${order.id}" placeholder="Your feedback" required>
              <input type="number" id="rating${order.id}" min="1" max="5" required>
              <button type="submit" class="success">✅ Submit Feedback</button>
            </form>` : ""}
        `;
                container.appendChild(div);
            });
        });
}

// =======================
// Feedback
// =======================
function submitFeedback(event, productId) {
    event.preventDefault();
    const orderId = event.target.querySelector("input").id.replace("comment", "");
    const feedback = {
        username: "Guest User",
        comment: document.getElementById(`comment${orderId}`).value,
        rating: parseInt(document.getElementById(`rating${orderId}`).value),
        product: { id: productId }
    };

    fetch(`${BASE_URL}/feedbacks`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(feedback)
    })
        .then(res => res.json())
        .then(() => alert("✅ Feedback submitted!"));
}

// =======================
// Admin Orders
// =======================
function loadAllOrdersForAdmin() {
    fetch(`${BASE_URL}/orders`)
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("adminOrdersList");
            if (!container) return;
            container.innerHTML = "";
            data.forEach(order => {
                const div = document.createElement("div");
                div.innerHTML = `
          <h3>Order #${order.id} <span style="color:#355CFF;">(${order.status})</span></h3>
          <p><b>${order.customerName}</b> (${order.customerEmail})</p>
          <ul>
            ${order.items.map(item => `<li>${item.product.name} (Qty: ${item.quantity})</li>`).join("")}
          </ul>
          <select id="status${order.id}">
            <option value="PENDING" ${order.status==="PENDING"?"selected":""}>Pending</option>
            <option value="CONFIRMED" ${order.status==="CONFIRMED"?"selected":""}>Confirmed</option>
            <option value="DELIVERED" ${order.status==="DELIVERED"?"selected":""}>Delivered</option>
            <option value="CANCELLED" ${order.status==="CANCELLED"?"selected":""}>Cancelled</option>
          </select>
          <button class="success" onclick="updateOrderStatus(${order.id})">✔ Update Status</button>
          <button class="warning" onclick="editOrder(${order.id}, '${order.customerName}', '${order.customerEmail}')">✏️ Edit</button>
          <button class="danger" onclick="deleteOrder(${order.id}, 'admin')">❌ Delete</button>
        `;
                container.appendChild(div);
            });
        });
}

function updateOrderStatus(orderId) {
    const status = document.getElementById(`status${orderId}`).value;
    fetch(`${BASE_URL}/orders/${orderId}/status?status=${status}`, { method: "PUT" })
        .then(res => res.json())
        .then(() => loadAllOrdersForAdmin());
}

// =======================
// Delete Order
// =======================
function deleteOrder(orderId, role) {
    if (confirm("Are you sure you want to delete this order?")) {
        fetch(`${BASE_URL}/orders/${orderId}`, { method: "DELETE" })
            .then(() => {
                alert("🗑 Order deleted!");
                if (role === "admin") {
                    loadAllOrdersForAdmin();
                } else {
                    loadOrders("guest@example.com");
                }
            });
    }
}

// =======================
// Edit Order (Admin)
// =======================
function editOrder(orderId, currentName, currentEmail) {
    const newName = prompt("Enter new customer name:", currentName);
    const newEmail = prompt("Enter new customer email:", currentEmail);

    if (newName && newEmail) {
        const updatedOrder = {
            customerName: newName,
            customerEmail: newEmail,
            status: document.getElementById(`status${orderId}`).value,
            items: []
        };

        fetch(`${BASE_URL}/orders/${orderId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedOrder)
        })
            .then(res => res.json())
            .then(() => {
                alert("✅ Order updated!");
                loadAllOrdersForAdmin();
            });
    }
}
