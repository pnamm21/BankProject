package com.bankapp.bankapp.app.entity;
;
import com.bankapp.bankapp.app.enums.StatusProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
        private StatusProduct status;

    @Column(name = "currency_code")
    private int currencyCode;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "limit")
    private int limit;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column(name = "updated_at")
    private LocalDate updated_at;

    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY,cascade = {PERSIST, MERGE, REFRESH})
    private Manager managerId;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = {PERSIST, MERGE, REFRESH})
    private List<Agreement> agreements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", managerId=" + managerId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", currencyCode=" + currencyCode +
                ", interestRate=" + interestRate +
                ", limit=" + limit +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}