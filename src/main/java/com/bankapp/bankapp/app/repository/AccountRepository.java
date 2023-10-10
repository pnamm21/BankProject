package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("select a from Account a where a.client.id = :id")
    List<Account> getListAccount(@Param("id") UUID id);

}
