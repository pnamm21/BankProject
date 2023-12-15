package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

/**
 * Product Mapper
 * @author Fam Le Duc Nam
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);

    Product productDtoPostToProduct(ProductDtoPost productDtoPost);

    Product mergeProduct(Product from, @MappingTarget Product to);

    Product productFullDtoToProduct(ProductDtoFullUpdate productDtoFullUpdate);

    List<ProductDto> listProductToListProductDto(List<Product> products);
}