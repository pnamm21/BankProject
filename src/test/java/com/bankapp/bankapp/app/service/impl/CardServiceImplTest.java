package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.TransactionDto;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardMapper cardMapper;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private static final UUID cardId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCardByIdTest() {

        Card mockCard = new Card();
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(mockCard));

        CardDto mockCardDto = new CardDto();
        when(cardMapper.cardToCardDto(mockCard)).thenReturn(mockCardDto);

        CardDto result = cardService.getCardById(cardId.toString());

        assertNotNull(result);
        assertEquals(mockCardDto, result);

        verify(cardRepository).findById(cardId);
        verify(cardMapper).cardToCardDto(mockCard);
    }

    @Test
    void getCardByIdNotFoundTest() {

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            cardService.getCardById(cardId.toString());
        });

        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void getListCardsTest() {

        List<Card> cards = Arrays.asList(new Card(), new Card());

        when(cardRepository.getCardsByAccount_Id(cardId)).thenReturn(cards);
        when(cardMapper.cardToCardDto(cards)).thenReturn(Arrays.asList(new CardDto(), new CardDto()));

        List<CardDto> result = cardService.getListCards(cardId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(cardRepository, times(1)).getCardsByAccount_Id(cardId);
        verify(cardMapper, times(1)).cardToCardDto(cards);
    }

    @Test
    void createMasterCardTest() {

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setCardNumber("1234567891011123");
        cardDtoPost.setCardHolder("Name");
        cardDtoPost.setType("MASTERCARD");
        cardDtoPost.setCvv("123");

        Card mockCard = new Card();
        when(cardMapper.cardDtoPostToCard(cardDtoPost)).thenReturn(mockCard);
        when(cardMapper.cardToCardDto(mockCard)).thenReturn(new CardDto());

        CardDto result = cardService.createMasterCard(cardDtoPost);

        assertNotNull(result);

        verify(cardRepository).save(mockCard);
        verify(cardMapper).cardDtoPostToCard(cardDtoPost);
        verify(cardMapper).cardToCardDto(mockCard);
    }

    @Test
    void createVisaCardTest() {

        CardDtoPost cardDtoPost = new CardDtoPost();
        cardDtoPost.setCardNumber("1234567891011123");
        cardDtoPost.setCardHolder("Name");
        cardDtoPost.setType("VISA");
        cardDtoPost.setCvv("123");

        Card mockCard = new Card();
        when(cardMapper.cardDtoPostToCard(cardDtoPost)).thenReturn(mockCard);
        when(cardMapper.cardToCardDto(mockCard)).thenReturn(new CardDto());

        CardDto result = cardService.createMasterCard(cardDtoPost);

        assertNotNull(result);

        verify(cardRepository).save(mockCard);
        verify(cardMapper).cardDtoPostToCard(cardDtoPost);
        verify(cardMapper).cardToCardDto(mockCard);

    }

    @Test
    public void testTransferByCardNumbers() {

        String fromCardNumber = "1234";
        String toCardNumber = "5678";
        double amount = 100.0;

        Card sourceCard = new Card();
        sourceCard.setCardNumber(fromCardNumber);

        Card destinationCard = new Card();
        destinationCard.setCardNumber(toCardNumber);

        Account fromAccount = new Account();
        fromAccount.setBalance(200.0);
        sourceCard.setAccount(fromAccount);

        Account toAccount = new Account();
        toAccount.setBalance(50.0);
        destinationCard.setAccount(toAccount);

        TransactionDtoTransferCard transferCard = new TransactionDtoTransferCard();
        transferCard.setAmount("100.0");
        transferCard.setTransactionType("TRANSFERS");
        transferCard.setDescription("Test Transfer");
        transferCard.setStatus("PENDING");

        Transaction transaction = new Transaction();
        transaction.setDebitAccount(fromAccount);
        transaction.setCreditAccount(toAccount);
        transaction.setTransactionType(TransactionType.TRANSFERS);
        transaction.setAmount(amount);
        transaction.setDescription("Test Transfer");
        transaction.setStatus(TransactionStatus.PENDING);

        TransactionDto expectedDto = new TransactionDto();
        expectedDto.setStatus("PENDING");
        expectedDto.setDescription("Test Transfer");

        when(cardRepository.findCardByCardNumber(fromCardNumber)).thenReturn(java.util.Optional.of(sourceCard));
        when(cardRepository.findCardByCardNumber(toCardNumber)).thenReturn(java.util.Optional.of(destinationCard));
        when(transactionMapper.transactionToTransactionDto(transaction)).thenReturn(expectedDto);

        TransactionDto result = cardService.transferByCardNumbers(fromCardNumber, toCardNumber, transferCard);

        verify(cardRepository, times(1)).findCardByCardNumber(fromCardNumber);
        verify(cardRepository, times(1)).findCardByCardNumber(toCardNumber);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(transactionMapper, times(1)).transactionToTransactionDto(any(Transaction.class));

        assertNotNull(result);
        assertEquals(expectedDto.getStatus(), result.getStatus());
        assertEquals(expectedDto.getDescription(), result.getDescription());
    }

}