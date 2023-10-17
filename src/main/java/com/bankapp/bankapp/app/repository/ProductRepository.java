package com.bankapp.bankapp.app.repository;

import com.bankapp.bankapp.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("select p from Product p where p.manager.id = :id")
    List<Product> getListProduct(@Param("id")UUID id);
}
