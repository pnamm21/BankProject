package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {
}
