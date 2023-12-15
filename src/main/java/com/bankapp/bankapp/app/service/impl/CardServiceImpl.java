package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.exception.*;
import com.bankapp.bankapp.app.generator.CardGenerator;
import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.entity.Transaction;
import com.bankapp.bankapp.app.entity.enums.CardStatus;
import com.bankapp.bankapp.app.entity.enums.CardType;
import com.bankapp.bankapp.app.entity.enums.TransactionStatus;
import com.bankapp.bankapp.app.entity.enums.TransactionType;
import com.bankapp.bankapp.app.mapper.CardMapper;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.repository.CardRepository;
import com.bankapp.bankapp.app.repository.TransactionRepository;
import com.bankapp.bankapp.app.service.util.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Card Service
 *
 * @author ffam5
 */
@Service
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     * @param cardMapper            Card Mapper
     * @param cardRepository        Card Repository
     * @param transactionRepository Transaction Repository
     * @param transactionMapper     Transaction Mapper
     */
    public CardServiceImpl(CardMapper cardMapper, CardRepository cardRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.cardMapper = cardMapper;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Find Card by ID
     * @param id CardID
     * @return CardDto or throw DataNotFoundException
     */
    @Override
    public CardDto getCardById(String id) throws DataNotFoundException {
        return cardMapper.cardToCardDto(cardRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    /**
     * Find List<Card> by AccountID
     * @param id AccountID
     * @return List<CardDto>
     */
    @Override
    public List<CardDto> getListCards(UUID id) {
        List<Card> cards = cardRepository.getCardsByAccount_Id(id);
        return cardMapper.cardToCardDto(cards);
    }

    /**
     * Create Master Card
     * @param cardDtoPost CardDtoPost
     * @return CardDto or throw DataNotFoundException
     */
    @Override
    @Transactional
    public CardDto createMasterCard(CardDtoPost cardDtoPost) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expirationDateStr = CardGenerator.generateExpirationDate().format(formatter);
        LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr, formatter);

        Card card = cardMapper.cardDtoPostToCard(cardDtoPost);
        card.setCardNumber(CardGenerator.generateRandomCardNumber(CardType.VISA));
        card.setCvv(CardGenerator.generateCVVCode());
        card.setExpirationDate(expirationDate);

        card.setCardHolder(cardDtoPost.getCardHolder());
        card.setType(CardType.MASTERCARD);
        card.setStatus(CardStatus.ACTIVE);

        cardRepository.save(card);
        return cardMapper.cardToCardDto(card);
    }

    /**
     * Create Visa Card
     * @param cardDtoPost CardDtoPost
     * @return CardDto or throw DataNotFoundException
     */
    @Override
    @Transactional
    public CardDto createVisaCard(CardDtoPost cardDtoPost) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expirationDateStr = CardGenerator.generateExpirationDate().format(formatter);
        LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr, formatter);

        Card card = cardMapper.cardDtoPostToCard(cardDtoPost);
        card.setCardNumber(CardGenerator.generateRandomCardNumber(CardType.VISA));
        card.setCvv(CardGenerator.generateCVVCode());
        card.setExpirationDate(expirationDate);

        card.setCardHolder(cardDtoPost.getCardHolder());
        card.setType(CardType.VISA);
        card.setStatus(CardStatus.ACTIVE);

        cardRepository.save(card);
        return cardMapper.cardToCardDto(card);
    }

    /**
     * Transfer by Card Numbers
     * @param from cardNumbers
     * @param to cardNumbers
     * @param transactionDtoTransferCard TransactionDtoTransferCard
     * @return TransactionDto or throw Transaction Error!
     */
    @Override
    @Transactional
    public TransactionDto transferByCardNumbers(String from, String to, TransactionDtoTransferCard transactionDtoTransferCard) {

        Card sourceCard = cardRepository.findCardByCardNumber(from)
                .orElseThrow(() -> new DataNotFoundException("Source card not found"));

        Card destinationCard = cardRepository.findCardByCardNumber(to)
                .orElseThrow(() -> new DataNotFoundException("Destination card not found"));

        Account fromAccount = sourceCard.getAccount();
        Account toAccount = destinationCard.getAccount();

        double amount = validateAndParseAmount(transactionDtoTransferCard.getAmount());

        if (fromAccount.getBalance() >= amount) {

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Transaction transaction = new Transaction();
            transaction.setDebitAccount(fromAccount);
            transaction.setCreditAccount(toAccount);
            transaction.setTransactionType(TransactionType.valueOf(transactionDtoTransferCard.getTransactionType()));
            transaction.setAmount(amount);
            transaction.setDescription(transactionDtoTransferCard.getDescription());
            transaction.setStatus(TransactionStatus.valueOf(transactionDtoTransferCard.getStatus()));
            transaction.setCreatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);

            return transactionMapper.transactionToTransactionDto(transaction);
        } else {
            TransactionDto unsuccessfulTransaction = new TransactionDto();
            unsuccessfulTransaction.setStatus("DECLINED");
            unsuccessfulTransaction.setDescription("Transaction Error!");
            return unsuccessfulTransaction;
        }
    }

    private double validateAndParseAmount(String amountString) {
        if (amountString == null || amountString.trim().isEmpty()) {
            throw new IllegalArgumentException("Amount cannot be null or empty");
        }

        try {
            return Double.parseDouble(amountString.trim());
        } catch (NumberFormatException e) {
            throw new InvalidTransactionAmountException("Invalid transaction amount");
        }
    }

}