package com.example.SuperMarketApp.repository;

import com.example.SuperMarketApp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
