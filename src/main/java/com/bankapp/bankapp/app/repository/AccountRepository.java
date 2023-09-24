package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
