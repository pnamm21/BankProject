package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.ClientDto;
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
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final ClientService clientService;
    private final ProductService productService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Manager> getManager(@PathVariable("id") String id) {
        return ResponseEntity.ok(managerService.getManagerById(id).orElse(null));
    }

    @RequestMapping(value = "/update-manager/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Manager> updateManager(@PathVariable("id") @IDChecker String id, @RequestBody ManagerDtoFullUpdate managerDtoFullUpdate) {
        return ResponseEntity.ofNullable(managerService.updateManager(id, managerDtoFullUpdate));
    }

    @RequestMapping("/get/all-clients")
    public ResponseEntity<List<ClientDto>> getClients(@RequestParam("id") @IDChecker String id){

        List<ClientDto> clientDtos = clientService.getListClients(UUID.fromString(id));
        return new ResponseEntity<>(clientDtos,HttpStatus.OK);
    }

    @RequestMapping("/get/all-products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam("id") @IDChecker String id){

        List<ProductDto> productDtos = productService.getListProduct(UUID.fromString(id));
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

}