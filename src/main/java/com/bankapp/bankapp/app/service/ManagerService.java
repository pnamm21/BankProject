package com.bankapp.bankapp.app.service;

import com.bankapp.bankapp.app.entity.Manager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ManagerService {

    Optional<Manager> getManagerById(String id);
}
