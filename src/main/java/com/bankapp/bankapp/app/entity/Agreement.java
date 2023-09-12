package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.StatusAgreement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "agreement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "status")
    private StatusAgreement status;

    @Column(name = "sum")
    private double sum;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @JoinColumn(name = "acount_id",referencedColumnName = "id")
    @OneToOne
    private Account accountId;

    @JoinColumn(name = "product_id",referencedColumnName = "id")
    @OneToOne
    private Product productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agreement agreement = (Agreement) o;
        return Objects.equals(id, agreement.id) && Objects.equals(accountId, agreement.accountId) && Objects.equals(productId, agreement.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, productId);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", productId=" + productId +
                ", interestRate=" + interestRate +
                ", status=" + status +
                ", sum=" + sum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}