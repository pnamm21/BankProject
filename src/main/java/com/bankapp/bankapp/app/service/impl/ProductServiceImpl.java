package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.entity.enums.ProductStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.ProductMapper;
import com.bankapp.bankapp.app.repository.ProductRepository;
import com.bankapp.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(UUID.fromString(id));
    }

    @Override
    public List<ProductDto> getListProduct(UUID id) {

        List<Product> products = productRepository.getProductsByManagerId(id);
        return productMapper.listProductToListProductDto(products);
    }

    @Override
    @Transactional
    public Product createProduct(ProductDtoPost productDtoPost) {

        Product product = productMapper.productDtoPostToProduct(productDtoPost);

        product.setName(productDtoPost.getName());
        product.setStatus(ProductStatus.valueOf(productDtoPost.getStatus()));
        product.setLimit(Integer.parseInt(productDtoPost.getLimit()));
        product.setInterestRate(Double.valueOf(productDtoPost.getInterestRate()));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(String id, ProductDtoFullUpdate productDtoFullUpdate) {

        UUID stringId = UUID.fromString(id);
        if (productRepository.existsById(stringId)) {
            productDtoFullUpdate.setManagerId(productDtoFullUpdate.getManagerId());
            productDtoFullUpdate.setId(id);
            Product product = productMapper.productFullDtoToProduct(productDtoFullUpdate);
            Product original = productRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            product.setManager(original.getManager());
            Product updated = productMapper.mergeProduct(product, original);
            return productRepository.save(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public String deleteProduct(String id) {

        UUID stringId = UUID.fromString(id);
        if (productRepository.existsById(stringId)) {
            Optional<Product> product = productRepository.findById(stringId);
            Product getProduct = product.get();
            getProduct.setStatus(ProductStatus.EXPIRED);
            productRepository.save(getProduct);
            return "Product has been EXPIRED";
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

}