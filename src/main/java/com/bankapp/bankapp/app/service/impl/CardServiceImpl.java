package com.bankapp.bankapp.app.service.impl;

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
import com.bankapp.bankapp.app.exception.BalanceIsEmptyException;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.CardMapper;
import com.bankapp.bankapp.app.repository.CardRepository;
import com.bankapp.bankapp.app.repository.TransactionRepository;
import com.bankapp.bankapp.app.service.util.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public CardServiceImpl(CardMapper cardMapper, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardMapper = cardMapper;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<Card> getCardById(String id) throws DataNotFoundException {
        return Optional.of(cardRepository.findById(UUID.fromString(id)))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    public List<CardDto> getListCards(UUID id) {
        List<Card> cards = cardRepository.getCardsByAccount_Id(id);
        return cardMapper.cardToCardDto(cards);
    }

    @Override
    @Transactional
    public Card createMasterCard(CardDtoPost cardDtoPost) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expirationDateStr = CardGenerator.generateExpirationDate().format(formatter);
        LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr, formatter);

        // Map DTO values to Card entity
        Card card = cardMapper.cardDtoPostToCard(cardDtoPost);
        card.setCardNumber(CardGenerator.generateRandomCardNumber(CardType.VISA));
        card.setCvv(CardGenerator.generateCVVCode());
        card.setExpirationDate(expirationDate);

        // Set other properties from DTO if needed
        card.setCardHolder(cardDtoPost.getCardHolder());
        card.setType(CardType.MASTERCARD);
        card.setStatus(CardStatus.ACTIVE);

        // Save the card to the repository
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public Card createVisaCard(CardDtoPost cardDtoPost) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expirationDateStr = CardGenerator.generateExpirationDate().format(formatter);
        LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr, formatter);

        // Map DTO values to Card entity
        Card card = cardMapper.cardDtoPostToCard(cardDtoPost);
        card.setCardNumber(CardGenerator.generateRandomCardNumber(CardType.VISA));
        card.setCvv(CardGenerator.generateCVVCode());
        card.setExpirationDate(expirationDate);

        // Set other properties from DTO if needed
        card.setCardHolder(cardDtoPost.getCardHolder());
        card.setType(CardType.VISA);
        card.setStatus(CardStatus.ACTIVE);

        // Save the card to the repository
        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public Transaction transferByCardNumbers(String from, String to, TransactionDtoTransferCard transactionDtoTransferCard) {

        // Find source card
        Card sourceCard = cardRepository.findCardByCardNumber(from)
                .orElseThrow(() -> new DataNotFoundException("Source card not found"));

        // Find destination card
        Card destinationCard = cardRepository.findCardByCardNumber(to)
                .orElseThrow(() -> new DataNotFoundException("Destination card not found"));

        // Get associated accounts
        Account fromAccount = sourceCard.getAccount();
        Account toAccount = destinationCard.getAccount();

        double amount = Double.parseDouble(transactionDtoTransferCard.getAmount());

        if (amount < 0) {
            throw new BalanceIsEmptyException("Account is empty");
        }

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
            return transaction;
        } else {
            throw new BalanceIsEmptyException("Account is empty");
        }
    }

}