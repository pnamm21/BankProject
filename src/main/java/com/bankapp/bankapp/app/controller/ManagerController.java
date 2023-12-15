package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ClientDto;
import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDto;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import com.bankapp.bankapp.app.service.ClientService;
import com.bankapp.bankapp.app.service.ManagerService;
import com.bankapp.bankapp.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Manager Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ClientService clientService;
    private final ProductService productService;

    @GetMapping("/get/{id}")
    public ManagerDto getManager(@PathVariable("id") String id) {
        return managerService.getManagerById(id);
    }

    @RequestMapping(value = "/update{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ManagerDto updateManager(@PathVariable("id") @IDChecker String id, @RequestBody ManagerDtoFullUpdate managerDtoFullUpdate) {
        return managerService.updateManager(id, managerDtoFullUpdate);
    }

    @RequestMapping("/clients")
    public List<ClientDto> getClients(@RequestParam("id") @IDChecker String id){
        return clientService.getListClients(UUID.fromString(id));
    }

    @RequestMapping("/products")
    public List<ProductDto> getProducts(@RequestParam("id") @IDChecker String id){
        return productService.getListProduct(UUID.fromString(id));
    }

}