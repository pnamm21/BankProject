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
import com.bankapp.bankapp.app.mapper.CardMapper;
import com.bankapp.bankapp.app.mapper.TransactionMapper;
import com.bankapp.bankapp.app.repository.CardRepository;
import com.bankapp.bankapp.app.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private static final UUID cardId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCardByIdTest() {

        Card card = new Card();
        card.setId(cardId);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        Optional<Card> result = cardService.getCardById(cardId.toString());

        assertTrue(result.isPresent());

        assertEquals(cardId, result.get().getId());

        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void getAccountByIdNotFoundTest() {

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(DataFormatException.class, () -> {
            cardService.getCardById(cardId.toString());
        });

        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void getListCardsTest() {

        List<Card> cards = Arrays.asList(new Card(), new Card());

        when(cardRepository.getCardsByAccount_Id(cardId)).thenReturn(cards);
        when(cardMapper.listCardToListCardDto(cards)).thenReturn(Arrays.asList(new CardDto(), new CardDto()));

        List<CardDto> result = cardService.getListCards(cardId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(cardRepository, times(1)).getCardsByAccount_Id(cardId);
        verify(cardMapper, times(1)).listCardToListCardDto(cards);
    }

    @Test
    void createMasterCardTest() {

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setCardNumber("1234567891011123");
        cardDtoPost.setCardHolder("Name");
        cardDtoPost.setType("VISA");
        cardDtoPost.setCvv("123");

        Card card = new Card();
        card.setId(cardId);

        when(cardMapper.cardDtoPostToCard(cardDtoPost)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardService.createVisaCard(cardDtoPost);

        assertNotNull(result);
        assertNotNull(result.getId());

        verify(cardMapper, times(1)).cardDtoPostToCard(cardDtoPost);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void createVisaCardTest() {

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setCardNumber("1234567891011123");
        cardDtoPost.setCardHolder("Name");
        cardDtoPost.setType("MASTERCARD");
        cardDtoPost.setCvv("123");

        Card card = new Card();
        card.setId(cardId);

        when(cardMapper.cardDtoPostToCard(cardDtoPost)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardService.createMasterCard(cardDtoPost);

        assertNotNull(result);
        assertNotNull(result.getId());

        verify(cardMapper, times(1)).cardDtoPostToCard(cardDtoPost);
        verify(cardRepository, times(1)).save(card);

    }

    @Test
    void testTransferByCardNumbers() {

        // Mock data
        String fromCardNumber = "4234567890123456";
        String toCardNumber = "4876543210987654";
        TransactionDtoTransferCard transactionDtoTransferCard = new TransactionDtoTransferCard();
        transactionDtoTransferCard.setFrom(fromCardNumber);
        transactionDtoTransferCard.setTo(toCardNumber);
        transactionDtoTransferCard.setAmount("50.0");
        transactionDtoTransferCard.setTransactionType("TRANSFERS");
        transactionDtoTransferCard.setDescription("Test Transfer");
        transactionDtoTransferCard.setStatus("APPROVED");

        Card sourceCard = new Card();
        sourceCard.setCardHolder("Name");
        sourceCard.setCardNumber(fromCardNumber);
        sourceCard.setCvv("123");
        sourceCard.setStatus(CardStatus.ACTIVE);
        sourceCard.setType(CardType.VISA);
        sourceCard.setExpirationDate(CardGenerator.generateExpirationDate());
        sourceCard.setCreatedAt(LocalDateTime.now());
        Account fromAccount = new Account();
        fromAccount.setBalance(100.0);
        sourceCard.setAccount(fromAccount);

        Card destinationCard = new Card();
        destinationCard.setCardHolder("Name");
        destinationCard.setCardNumber(toCardNumber);
        destinationCard.setCvv("123");
        destinationCard.setStatus(CardStatus.ACTIVE);
        destinationCard.setType(CardType.VISA);
        destinationCard.setExpirationDate(CardGenerator.generateExpirationDate());
        destinationCard.setCreatedAt(LocalDateTime.now());
        Account toAccount = new Account();
        toAccount.setBalance(50.0);
        destinationCard.setAccount(toAccount);

        when(cardRepository.findCardByCardNumber(fromCardNumber)).thenReturn(Optional.of(sourceCard));
        when(cardRepository.findCardByCardNumber(toCardNumber)).thenReturn(Optional.of(destinationCard));

        // Invoke the method
        Transaction resultTransaction = cardService.transferByCardNumbers(fromCardNumber,toCardNumber,transactionDtoTransferCard);

        // Assertions
//        assertNotNull(resultTransaction);
        assertEquals(fromAccount.getBalance(), 50.0); // Check if the balance was updated correctly for the source account
        assertEquals(toAccount.getBalance(), 100.0); // Check if the balance was updated correctly for the destination account
        assertEquals(TransactionType.TRANSFERS, resultTransaction.getTransactionType());
        assertEquals(50.0, resultTransaction.getAmount());
        assertEquals("Test Transfer", resultTransaction.getDescription());
        assertEquals(TransactionStatus.APPROVED, resultTransaction.getStatus());

        verify(cardService, times(1)).transferByCardNumbers(fromCardNumber, toCardNumber, transactionDtoTransferCard);
    }

}