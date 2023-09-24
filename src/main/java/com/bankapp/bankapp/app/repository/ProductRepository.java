package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
