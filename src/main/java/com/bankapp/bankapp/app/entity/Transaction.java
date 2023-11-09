package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.TransactionStatus;
import com.bankapp.bankapp.app.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JoinColumn(name = "debit_account_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Account debitAccount;

    @JoinColumn(name = "credit_account_id",referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Account creditAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", debitAccountId=" + debitAccount +
                ", creditAccountId=" + creditAccount +
                ", transaction_type=" + transactionType +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}