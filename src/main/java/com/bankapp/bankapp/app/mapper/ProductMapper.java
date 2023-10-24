package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",uses = UUID.class)
public interface ProductMapper {
    ProductDto productToProductDto(Product product);
    Product productDtoToProduct(ProductDto productDto);
    Product productDtoPostToProduct(ProductDtoPost productDtoPost);
    List<ProductDto> listProductToListProductDto(List<Product> products);
}
