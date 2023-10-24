package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.repository.ManagerRepository;
import com.bankapp.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public Optional<Manager> getManagerById(String id) {
        return managerRepository.findById(UUID.fromString(id));
    }

}