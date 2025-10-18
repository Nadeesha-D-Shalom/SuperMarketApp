package com.example.SuperMarketApp.repository;

import com.example.SuperMarketApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
