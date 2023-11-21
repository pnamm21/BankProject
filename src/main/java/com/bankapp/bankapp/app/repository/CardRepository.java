package com.bankapp.bankapp.app.repository;


import com.bankapp.bankapp.app.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    @Query("select c from Card c where c.account.id = :id")
    List<Card> getListCards(@Param("id") UUID id);

    @Query("select c from Card c where c.cardNumber = :cardNumber")
    Optional<Card> findByCardNumber(@Param("cardNumber") String cardNumber);

}