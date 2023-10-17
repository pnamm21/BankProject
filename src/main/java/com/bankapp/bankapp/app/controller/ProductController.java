package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.AccountDto;
import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.ProductMapper;
import com.bankapp.bankapp.app.service.ManagerService;
import com.bankapp.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ManagerService managerService;
    private final ProductMapper productMapper;

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProductId(@PathVariable("id") String id) {
        Optional<Product> optionalProduct;
        try {
            optionalProduct = productService.getProductById(id);
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        if (optionalProduct.isEmpty()) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
        return new ResponseEntity<>(productMapper.productToProductDto(optionalProduct.get()), HttpStatus.OK);
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDto>> getListProductsByManagerId(@RequestParam("id") String id) {
        return ResponseEntity.ofNullable(productMapper.listProductToListProductDto(productService.getListProduct(UUID.fromString(id))));
    }

    @RequestMapping(value = "/create-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ProductDto createProduct(@RequestBody ProductDtoPost productDtoPost) {
        productDtoPost.setId(UUID.randomUUID().toString());
        Product product = productMapper.productDtoPostToProduct(productDtoPost);
        product.setManager(managerService.getManagerById(productDtoPost.getManagerId()).orElseThrow());
        productService.createProduct(product);
        return productMapper.productToProductDto(product);
    }

}