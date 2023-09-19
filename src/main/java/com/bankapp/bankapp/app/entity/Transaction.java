package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.enums.StatusTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "type")
    private int type;

    @Column(name = "amount")
    private double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private StatusTransaction status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @JoinColumn(name = "debit_account_id",referencedColumnName = "id")
    @ManyToOne(cascade = {PERSIST, MERGE, REFRESH})
    private Account debitAccountId;

    @JoinColumn(name = "credit_account_id",referencedColumnName = "id")
    @ManyToOne(cascade = {PERSIST, MERGE, REFRESH})
    private Account creditAccountId;

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
                ", debitAccountId=" + debitAccountId +
                ", creditAccountId=" + creditAccountId +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}