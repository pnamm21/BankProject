package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Product Service
 * @author Fam Le Duc Nam
 */
@Service
public interface ProductService {

    ProductDto getProductById(String id);
    @Transactional
    ProductDto createProduct(ProductDtoPost productDtoPost);
    List<ProductDto> getListProduct(@Param("id")UUID id);

    @Transactional
    ProductDto updateProduct(String id, ProductDtoFullUpdate productDtoFullUpdate);

    @Transactional
    String deleteProduct(String id);
}
