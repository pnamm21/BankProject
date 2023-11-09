package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.ManagerMapper;
import com.bankapp.bankapp.app.repository.ManagerRepository;
import com.bankapp.bankapp.app.service.ManagerService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public Optional<Manager> getManagerById(String id) throws DataNotFoundException {
        return Optional.ofNullable(managerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    @Override
    public Manager updateManager(String id, ManagerDtoFullUpdate managerDtoFullUpdate) {
        UUID stringId = UUID.fromString(id);
        if (managerRepository.existsById(stringId)) {
            managerDtoFullUpdate.setId(id);
            Manager account = managerMapper.managerDtoFullToManager(managerDtoFullUpdate);
            Manager original = managerRepository.findById(stringId)
                    .orElseThrow(()->new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            Manager updated = managerMapper.mergeManagers(account, original);
            return managerRepository.save(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }



}