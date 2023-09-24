package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
