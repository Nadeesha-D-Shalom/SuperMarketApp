package com.example.SuperMarketApp.controller;

import com.example.SuperMarketApp.model.Order;
import com.example.SuperMarketApp.model.OrderStatus;
import com.example.SuperMarketApp.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {
    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    // ✅ Create new order
    @PostMapping
    public Order create(@RequestBody Order order) {
        return repo.save(order);
    }

    // ✅ Get orders by customer email
    @GetMapping("/track")
    public List<Order> getByEmail(@RequestParam String email) {
        return repo.findByCustomerEmail(email);
    }

    // ✅ Get all orders (Admin)
    @GetMapping
    public List<Order> getAll() {
        return repo.findAll();
    }

    // ✅ Update only order status
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        Order order = repo.findById(id).orElseThrow();
        order.setStatus(status);
        return repo.save(order);
    }

    // ✅ Edit entire order (Admin)
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order order = repo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        order.setCustomerName(updatedOrder.getCustomerName());
        order.setCustomerEmail(updatedOrder.getCustomerEmail());
        order.setStatus(updatedOrder.getStatus());

        // Replace items if provided
        if (updatedOrder.getItems() != null && !updatedOrder.getItems().isEmpty()) {
            order.getItems().clear();
            updatedOrder.getItems().forEach(item -> item.setOrder(order));
            order.getItems().addAll(updatedOrder.getItems());
        }

        return repo.save(order);
    }

    // ✅ Delete order (User/Admin)
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
