package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgreementRepository extends JpaRepository<Agreement, UUID> {
}
