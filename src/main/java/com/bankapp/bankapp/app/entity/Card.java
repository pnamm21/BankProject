package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.CardStatus;
import com.bankapp.bankapp.app.entity.enums.CardType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @Column(name = "card_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "card_holder")
    private String cardHolder;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(name = "card_status")
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JoinColumn(name = "account_id",referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardNumber, card.cardNumber) && Objects.equals(cvv, card.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, cvv);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardHolder='" + cardHolder + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", expirationDate=" + expirationDate +
                ", createdAt=" + createdAt +
                '}';
    }
}