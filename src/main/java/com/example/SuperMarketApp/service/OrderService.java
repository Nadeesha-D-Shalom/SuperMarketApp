package com.example.SuperMarketApp.service;

import com.example.SuperMarketApp.model.Order;
import com.example.SuperMarketApp.model.OrderItem;
import com.example.SuperMarketApp.model.OrderStatus;
import com.example.SuperMarketApp.repository.OrderRepository;
import com.example.SuperMarketApp.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderService(OrderRepository orderRepo, OrderItemRepository orderItemRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public Order placeOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }
        return orderRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepo.findById(id);
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepo.findByCustomerEmail(email);
    }

    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));

            return orderRepo.save(order);
        }
        return null;
    }
}
