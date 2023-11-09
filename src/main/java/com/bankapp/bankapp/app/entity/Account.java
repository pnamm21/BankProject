package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.AccountStatus;
import com.bankapp.bankapp.app.entity.enums.AccountType;
import com.bankapp.bankapp.app.entity.enums.CurrencyCodeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_name")
    private String name;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCodeType currencyCode;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JoinColumn(name = "client_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Client client;

    @OneToMany
    @JsonIgnore
    private Set<Card> cards;

    @OneToMany
    @JsonIgnore
    private List<Agreement> agreements;

    @OneToMany
    @JsonIgnore
    private Set<Transaction> debitAccount;

    @OneToMany
    @JsonIgnore
    private Set<Transaction> creditAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + client +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", balance=" + balance +
                ", currencyCode=" + currencyCode +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}