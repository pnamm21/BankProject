package com.bankapp.bankapp.app.service.util;

import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransfer;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CardService {

    Optional<Card> getCardById(String id) throws DataNotFoundException;

    List<CardDto> getListCards(@Param("id")UUID id);

    @Transactional
    Card createMasterCard(CardDtoPost cardDtoPost);

    @Transactional
    Card createVisaCard(CardDtoPost cardDtoPost);

    @Transactional
    Transaction transferByCardNumbers(String from, String to, TransactionDtoTransferCard transactionDtoTransferCard);
}