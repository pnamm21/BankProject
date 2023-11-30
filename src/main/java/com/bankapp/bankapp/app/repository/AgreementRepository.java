package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

   List<Agreement> getAgreementByAccountId(@Param("id") UUID id);

}