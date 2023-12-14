package com.bankapp.bankapp.app.controller;

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
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductId(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(productService.getProductById(id).orElse(null));
    }

    @RequestMapping(value = "/create-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDtoPost productDtoPost) {
        productDtoPost.setId(UUID.randomUUID().toString());

        return new ResponseEntity<>(productService.createProduct(productDtoPost), HttpStatus.OK);
    }

    @RequestMapping(value = "/update-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Product> updateProduct(@PathVariable("id") @IDChecker String id, @RequestBody ProductDtoFullUpdate productDtoFullUpdate) {
        return ResponseEntity.ofNullable(productService.updateProduct(id, productDtoFullUpdate));
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<String> deleteProduct(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

}