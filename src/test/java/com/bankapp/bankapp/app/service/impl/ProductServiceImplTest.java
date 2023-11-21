package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.AccountDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.entity.enums.AccountStatus;
import com.bankapp.bankapp.app.entity.enums.AccountType;
import com.bankapp.bankapp.app.entity.enums.CurrencyCodeType;
import com.bankapp.bankapp.app.entity.enums.ProductStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.ProductMapper;
import com.bankapp.bankapp.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private static final UUID productId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductByIdTest() {

        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(productId.toString());

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getId());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductByIdNotFoundTest() {

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(productId.toString());

        assertFalse(result.isPresent());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getListProductTest() {

        List<Product> mockProducts = Arrays.asList(new Product(), new Product());

        when(productRepository.getListProduct(productId)).thenReturn(mockProducts);
        when(productMapper.listProductToListProductDto(mockProducts)).thenReturn(Arrays.asList(new ProductDto(), new ProductDto()));

        List<ProductDto> result = productService.getListProduct(productId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(productRepository, times(1)).getListProduct(productId);
        verify(productMapper, times(1)).listProductToListProductDto(mockProducts);
    }

    @Test
    void createProductTest() {

        ProductDtoPost productDtoPost = new ProductDtoPost();
        productDtoPost.setName("Simple Product");
        productDtoPost.setStatus("ACTIVE");
        productDtoPost.setLimit("1000");
        productDtoPost.setInterestRate("5");

        Product product = new Product();
        product.setId(productId);
        product.setName(productDtoPost.getName());
        product.setStatus(ProductStatus.ACTIVE);
        product.setInterestRate(5.0);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        when(productMapper.productDtoPostToProduct(productDtoPost)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(productDtoPost);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(productDtoPost.getName(), result.getName());
        assertEquals(ProductStatus.ACTIVE, result.getStatus());
        assertEquals(1000.0, result.getLimit());
        assertEquals(5.0, result.getInterestRate());

        verify(productMapper, times(1)).productDtoPostToProduct(productDtoPost);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProductTest() {

        ProductDtoFullUpdate productDtoFullUpdate = new ProductDtoFullUpdate();
        productDtoFullUpdate.setName("Name");
        productDtoFullUpdate.setStatus("ACTIVE");
        productDtoFullUpdate.setCurrencyCode("USD");
        productDtoFullUpdate.setInterestRate("0.0001");
        productDtoFullUpdate.setLimit("1000");
        productDtoFullUpdate.setManagerId(String.valueOf(UUID.randomUUID()));

        Product originalProduct = new Product();
        originalProduct.setId(productId);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName(productDtoFullUpdate.getName());
        updatedProduct.setStatus(ProductStatus.valueOf(productDtoFullUpdate.getStatus()));
        updatedProduct.setCurrencyCode(CurrencyCodeType.valueOf(productDtoFullUpdate.getCurrencyCode()));
        updatedProduct.setInterestRate(Double.valueOf(productDtoFullUpdate.getInterestRate()));
        updatedProduct.setLimit(Integer.parseInt(productDtoFullUpdate.getLimit()));

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(originalProduct));
        when(productMapper.productFullDtoToProduct(productDtoFullUpdate)).thenReturn(updatedProduct);
        when(productMapper.mergeProduct(updatedProduct, originalProduct)).thenReturn(updatedProduct);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(productId.toString(), productDtoFullUpdate);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(productDtoFullUpdate.getName(), result.getName());
        assertEquals(productDtoFullUpdate.getStatus(), result.getStatus().toString());
        assertEquals(productDtoFullUpdate.getCurrencyCode(), result.getCurrencyCode().toString());
        assertEquals(Double.parseDouble(productDtoFullUpdate.getInterestRate()), result.getInterestRate());
        assertEquals(productDtoFullUpdate.getLimit(), String.valueOf(result.getLimit()));

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).productFullDtoToProduct(productDtoFullUpdate);
        verify(productMapper, times(1)).mergeProduct(updatedProduct, originalProduct);
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void updateProductNotFound(){

        ProductDtoFullUpdate productDtoFullUpdate = new ProductDtoFullUpdate();

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(DataNotFoundException.class,()->{
           productService.updateProduct(productId.toString(),productDtoFullUpdate);
        });

        verify(productRepository,times(1)).existsById(productId);
        verify(productRepository,never()).findById(productId);
        verify(productMapper, never()).productFullDtoToProduct(productDtoFullUpdate);
        verify(productMapper,never()).mergeProduct(any(),any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProductTest(){

        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setStatus(ProductStatus.ACTIVE);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        String result = productService.deleteProduct(productId.toString());

        assertEquals("Product has been EXPIRED",result);
        assertEquals(ProductStatus.EXPIRED,mockProduct.getStatus());

        verify(productRepository,times(1)).existsById(productId);
        verify(productRepository,times(1)).findById(productId);
        verify(productRepository,times(1)).save(mockProduct);
    }

    @Test
    void deleteProductNotFoundTest(){

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(DataNotFoundException.class,()->{
           productService.deleteProduct(productId.toString());
        });

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository,never()).findById(productId);
        verify(productRepository,never()).save(any());
    }


}