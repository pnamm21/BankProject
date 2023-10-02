package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.entity.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductService {

    Optional<Product> getProductById(String id);
    Product createProduct(Product product);
}
