package com.bankapp.bankapp.app.service.util;

import com.bankapp.bankapp.app.dto.*;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Card Service
 * @author Fam Le Duc Nam
 */
@Service
public interface CardService {

    CardDto getCardById(String id) throws DataNotFoundException;

    List<CardDto> getListCards(@Param("id")UUID id);

    @Transactional
    CardDto createMasterCard(CardDtoPost cardDtoPost);

    @Transactional
    CardDto createVisaCard(CardDtoPost cardDtoPost);

    @Transactional
    TransactionDto transferByCardNumbers(String from, String to, TransactionDtoTransferCard transactionDtoTransferCard);
}