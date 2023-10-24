package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.entity.enums.ProductStatus;
import com.bankapp.bankapp.app.mapper.ProductMapper;
import com.bankapp.bankapp.app.repository.ProductRepository;
import com.bankapp.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Optional<Product> getProductById(String id) {
        try {
            return productRepository.findById(UUID.fromString(id));
        } catch (Exception e) {
            throw new NoSuchElementException("Product with this id not found");
        }
    }

    @Override
    public List<ProductDto> getListProduct(UUID id){

        List<Product> products = productRepository.getListProduct(id);
        return productMapper.listProductToListProductDto(products);
    }

    @Override
    @Transactional
    public Product createProduct(ProductDtoPost productDtoPost) {

        Product product = productMapper.productDtoPostToProduct(productDtoPost);

        product.setName(productDtoPost.getName());
        product.setStatus(ProductStatus.valueOf(productDtoPost.getStatus()));
        product.setLimit(Double.valueOf(productDtoPost.getLimit()));
        product.setInterestRate(Double.valueOf(productDtoPost.getInterestRate()));
        product.setCreated_at(LocalDateTime.now());
        product.setUpdated_at(LocalDateTime.now());
        productRepository.save(product);
        return product;
    }

}
