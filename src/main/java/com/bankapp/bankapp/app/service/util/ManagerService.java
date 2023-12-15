package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Manager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Manager Service
 * @author Fam Le Duc Nam
 */
@Service
public interface ManagerService {

    ManagerDto getManagerById(String id);

    @Transactional
    ManagerDto updateManager(String id, ManagerDtoFullUpdate managerDtoFullUpdate);

}