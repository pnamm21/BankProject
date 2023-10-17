package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ProductService {

    Optional<Product> getProductById(String id);
    void createProduct(Product product);
    List<Product> getListProduct(@Param("id")UUID id);
}
