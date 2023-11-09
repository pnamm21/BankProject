package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.AgreementStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "agreements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AgreementStatus status;

    @Column(name = "sum")
    private Double sum;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Account account;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agreement agreement = (Agreement) o;
        return Objects.equals(id, agreement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", interestRate=" + interestRate +
                ", status=" + status +
                ", sum=" + sum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}