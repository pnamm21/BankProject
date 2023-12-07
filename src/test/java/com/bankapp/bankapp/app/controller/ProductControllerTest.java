package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoPost;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private static final UUID productId = UUID.randomUUID();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductIdTest() {

        Product product = new Product();
        product.setId(productId);
        Mockito.when(productService.getProductById(productId.toString())).thenReturn(Optional.of(product));

        String responseContent;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/product/get/{id}", productId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/product/get/{id}", productId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId.toString()));  // Expect the correct account ID in the JSON response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void createProductTest() {

        ProductDtoPost productDtoPost = new ProductDtoPost();
        productDtoPost.setName("John Doe");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/product/create-product")
                            .content(asJsonString(productDtoPost))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());  // Expect a successful response
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void updateProductTest() {

        ProductDtoFullUpdate productDtoFullUpdate = new ProductDtoFullUpdate();
        productDtoFullUpdate.setName("Updated Name");

        Product product = new Product();
        product.setId(productId);  // Set the ID in the mock response
        Mockito.when(productService.updateProduct(productId.toString(), productDtoFullUpdate)).thenReturn(product);

        String responseContent;
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/product/update-product/{id}", productId)
                            .content(asJsonString(productDtoFullUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Content: " + responseContent);

        verify(productService).updateProduct(productId.toString(), productDtoFullUpdate);  // Verify that the service method was called with the correct ID and DTO

    }

    @Test
    void deleteProductTest() {

        Mockito.when(productService.deleteProduct(productId.toString())).thenReturn("Product deleted successfully");

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/product/delete-product/{id}", productId))
                    .andExpect(status().isOk())  // Expect a successful response
                    .andExpect(MockMvcResultMatchers.content().string("Product deleted successfully"));  // Expect the correct response message
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(productService).deleteProduct(productId.toString());  // Verify that the service method was called with the correct ID

    }

    // Helper method to convert objects to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}