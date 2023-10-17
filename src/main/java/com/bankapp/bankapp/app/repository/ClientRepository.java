package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query("select c from Client c where c.manager.id = :id")
    List<Client> getListClient(@Param("id")UUID id);
}
