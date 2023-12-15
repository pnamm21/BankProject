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
        ProductDto expectedProductDto = new ProductDto();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDto(product)).thenReturn(expectedProductDto);

        ProductDto result = productService.getProductById(productId.toString());

        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).productToProductDto(product);
    }

    @Test
    void getProductByIdNotFoundTest() {

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            productService.getProductById(productId.toString());
        });

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getListProductTest() {

        List<Product> mockProducts = Arrays.asList(new Product(), new Product());

        when(productRepository.getProductsByManagerId(productId)).thenReturn(mockProducts);
        when(productMapper.listProductToListProductDto(mockProducts)).thenReturn(Arrays.asList(new ProductDto(), new ProductDto()));

        List<ProductDto> result = productService.getListProduct(productId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(productRepository, times(1)).getProductsByManagerId(productId);
        verify(productMapper, times(1)).listProductToListProductDto(mockProducts);
    }

    @Test
    void createProductTest() {

        ProductDtoPost productDtoPost = new ProductDtoPost();
        Product product = new Product();
        ProductDto expectedProductDto = new ProductDto();

        when(productMapper.productDtoPostToProduct(productDtoPost)).thenReturn(product);
        when(productMapper.productToProductDto(product)).thenReturn(expectedProductDto);

        ProductDto result = productService.createProduct(productDtoPost);

        assertNotNull(result);

        verify(productMapper, times(1)).productDtoPostToProduct(productDtoPost);
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).productToProductDto(product);
    }

    @Test
    void updateProductTest() {

        ProductDtoFullUpdate productDtoFullUpdate = new ProductDtoFullUpdate();
        Product product = new Product();
        Product originalProduct = new Product();
        Product updatedProduct = new Product();
        ProductDto expectedProductDto = new ProductDto();

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(originalProduct));
        when(productMapper.productFullDtoToProduct(productDtoFullUpdate)).thenReturn(product);
        when(productMapper.mergeProduct(product, originalProduct)).thenReturn(updatedProduct);
        when(productMapper.productToProductDto(updatedProduct)).thenReturn(expectedProductDto);

        ProductDto result = productService.updateProduct(productId.toString(), productDtoFullUpdate);

        assertNotNull(result);

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(updatedProduct);
        verify(productMapper, times(1)).productFullDtoToProduct(productDtoFullUpdate);
        verify(productMapper, times(1)).mergeProduct(product, originalProduct);
        verify(productMapper, times(1)).productToProductDto(updatedProduct);

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