package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.exception.InvalidUUIDException;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.mapper.ProductMapper;
import com.bankapp.bankapp.app.service.ManagerService;
import com.bankapp.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/get/{id}")
    public Optional<ResponseEntity<Product>> getProductId( @PathVariable("id")@IDChecker String id) {

        try {
            Optional<Product> optionalProduct = productService.getProductById(id);
            return optionalProduct.map(product -> new ResponseEntity<>(product,HttpStatus.OK));
        } catch (Exception e) {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }

    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDto>> getListProductsByManagerId( @RequestParam("id") @IDChecker String id) {

        List<ProductDto> productDtos = productService.getListProduct(UUID.fromString(id));
        return ResponseEntity.ofNullable(productDtos);
    }

    @RequestMapping(value = "/create-product", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDtoPost productDtoPost) {
        productDtoPost.setId(UUID.randomUUID().toString());

        return new ResponseEntity<>(productService.createProduct(productDtoPost), HttpStatus.OK);
    }

}