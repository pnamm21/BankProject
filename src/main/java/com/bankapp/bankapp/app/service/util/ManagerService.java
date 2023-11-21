package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Manager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public interface ManagerService {

    Optional<Manager> getManagerById(String id);

    @Transactional
    Manager updateManager(String id, ManagerDtoFullUpdate managerDtoFullUpdate);

}