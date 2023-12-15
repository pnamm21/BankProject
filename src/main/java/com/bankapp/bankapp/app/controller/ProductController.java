package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Product Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/get/{id}")
    public ProductDto getProductId(@PathVariable("id") @IDChecker String id) {
        return productService.getProductById(id);
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST, RequestMethod.GET})
    public ProductDto createProduct(@RequestBody @Valid ProductDtoPost productDtoPost) {
        return productService.createProduct(productDtoPost);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ProductDto updateProduct(@PathVariable("id") @IDChecker String id, @RequestBody ProductDtoFullUpdate productDtoFullUpdate) {
        return productService.updateProduct(id, productDtoFullUpdate);
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteProduct(@PathVariable("id") @IDChecker String id) {
        return productService.deleteProduct(id);
    }

}