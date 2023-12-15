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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Product Service
 * @author ffam5
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * @param productRepository Product Repository
     * @param productMapper Product Mapper
     */
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Find Product by ID
     * @param id ProductID
     * @return ProductDto or throw DataNotFoundException
     */
    @Override
    public ProductDto getProductById(String id) {
        return productMapper.productToProductDto(productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    /**
     * Find List<Product> by ManagerID
     * @param id ManagerID
     * @return List<ProductDto>
     */
    @Override
    public List<ProductDto> getListProduct(UUID id) {

        List<Product> products = productRepository.getProductsByManagerId(id);
        return productMapper.listProductToListProductDto(products);
    }

    /**
     * Create Product
     * @param productDtoPost ProductDtoPost
     * @return ProductDto or throw DataNotFoundException
     */
    @Override
    @Transactional
    public ProductDto createProduct(ProductDtoPost productDtoPost) {

        Product product = productMapper.productDtoPostToProduct(productDtoPost);

        productRepository.save(product);
        return productMapper.productToProductDto(product);
    }

    /**
     * Update Product
     * @param id ProductID
     * @param productDtoFullUpdate ProductDtoFullUpdate
     * @return ProductDto throw DataNotFoundException
     */
    @Override
    @Transactional
    public ProductDto updateProduct(String id, ProductDtoFullUpdate productDtoFullUpdate) {

        UUID stringId = UUID.fromString(id);
        if (productRepository.existsById(stringId)) {
            productDtoFullUpdate.setManagerId(productDtoFullUpdate.getManagerId());
            productDtoFullUpdate.setId(id);
            Product product = productMapper.productFullDtoToProduct(productDtoFullUpdate);
            Product original = productRepository.findById(stringId)
                    .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            product.setManager(original.getManager());
            Product updated = productMapper.mergeProduct(product, original);
            productRepository.save(updated);

            return productMapper.productToProductDto(product);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }

    /**
     * Delete Product
     * @param id ProductID
     * @return "Product has been EXPIRED"
     */
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