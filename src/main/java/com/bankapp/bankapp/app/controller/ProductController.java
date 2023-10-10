package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.mapper.ProductMapper;
import com.bankapp.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/get/{id}")
    public ProductDto getProduct(@PathVariable("id")String id){
        return productMapper.productToProductDto(productService.getProductById(id).orElseThrow());
    }

    @PostMapping("/create-product")
    public Product createProduct(Product product) {
        return productService.createProduct(product);
    }

}
