package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.*;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private static final UUID productId = UUID.randomUUID();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void getProductIdTest() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setId(String.valueOf(productId));

        String responseContent = null;
        String json = objectMapper.writeValueAsString(productDto);

        responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/get/{id}", productId)
                        .secure(true))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/get/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void createProductTest() {

        ProductDtoPost productDtoPost = new ProductDtoPost();
        productDtoPost.setName("John Doe");

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
                            .content(asJsonString(productDtoPost))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void updateProductTest() throws Exception {

        ProductDtoFullUpdate productDtoFullUpdate = new ProductDtoFullUpdate();
        productDtoFullUpdate.setName("Updated Name");

        Product product = new Product();
        product.setId(UUID.fromString(String.valueOf(productId)));

        String responseContent;

            responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/api/product/update/{id}", productId)
                            .content(asJsonString(productDtoFullUpdate))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

        System.out.println("Response Content: " + responseContent);

        verify(productService).updateProduct(productId.toString(), productDtoFullUpdate);

    }

    @Test
    @WithMockUser(username = "nam",roles = "USER")
    void deleteProductTest() throws Exception {

        Mockito.when(productService.deleteProduct(productId.toString())).thenReturn("Product deleted successfully");

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/delete/{id}", productId))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Product deleted successfully"));

        verify(productService).deleteProduct(productId.toString());

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}