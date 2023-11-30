package com.bankapp.bankapp.app.repository;


import com.bankapp.bankapp.app.entity.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    List<Card> getCardsByAccount_Id(@Param("id") UUID id);

    Optional<Card> findCardByCardNumber(@Param("cardNumber") String cardNumber);


}