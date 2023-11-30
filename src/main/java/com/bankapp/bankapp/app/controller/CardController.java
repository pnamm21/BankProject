package com.bankapp.bankapp.app.controller;

import com.bankapp.bankapp.app.dto.CardDtoPost;
import com.bankapp.bankapp.app.dto.TransactionDtoTransferCard;
import com.bankapp.bankapp.app.entity.Card;
import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;

import com.bankapp.bankapp.app.service.util.CardService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Card> getCard(@PathVariable("id") @IDChecker String id) {
        return ResponseEntity.ok(cardService.getCardById(id).orElse(null));
    }

    @RequestMapping(value = "/create-master-card", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Card> createMasterCard(@RequestBody CardDtoPost cardDtoPost) {
        return new ResponseEntity<>(cardService.createMasterCard(cardDtoPost), HttpStatus.OK);
    }

    @RequestMapping(value = "/create-visa-card", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Card> createVisaCard(@RequestBody CardDtoPost cardDtoPost) {
        return new ResponseEntity<>(cardService.createVisaCard(cardDtoPost), HttpStatus.OK);
    }

    @RequestMapping(value = "/card-transfer/{cardNumber}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> transferByCardNumber(@PathVariable("cardNumber") String cardNumber,@RequestBody TransactionDtoTransferCard transactionDtoTransferCard) {
        cardService.transferByCardNumbers(cardNumber, transactionDtoTransferCard.getTo(), transactionDtoTransferCard);
        return ResponseEntity.ok("Transfer successful");
    }

}