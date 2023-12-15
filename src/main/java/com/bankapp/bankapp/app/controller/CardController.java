package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.CardDto;
import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;

import com.bankapp.bankapp.app.service.util.CardService;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Card Controller
 * @author Fam Le Duc Nam
 */
@Validated
@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/get/{id}")
    public CardDto getCard(@PathVariable("id") @IDChecker String id) {
        return cardService.getCardById(id);
    }

    @RequestMapping(value = "/create-master-card", method = {RequestMethod.POST, RequestMethod.GET})
    public CardDto createMasterCard(@RequestBody CardDtoPost cardDtoPost) {
        return cardService.createMasterCard(cardDtoPost);
    }

    @RequestMapping(value = "/create-visa-card", method = {RequestMethod.POST, RequestMethod.GET})
    public CardDto createVisaCard(@RequestBody CardDtoPost cardDtoPost) {
        return cardService.createVisaCard(cardDtoPost);
    }

    @RequestMapping(value = "/card-transfer/{cardNumber}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> transferByCardNumber(@PathVariable("cardNumber") String cardNumber,@RequestBody TransactionDtoTransferCard transactionDtoTransferCard) {
        cardService.transferByCardNumbers(cardNumber, transactionDtoTransferCard.getTo(), transactionDtoTransferCard);
        return ResponseEntity.ok("Transfer successful");
    }

}