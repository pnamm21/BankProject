package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.StatusClient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.MERGE;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "status")
    private StatusClient status;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column(name = "updated_at")
    private LocalDate updated_at;

    @JoinColumn(name = "manager_id",referencedColumnName = "id")
    @OneToOne
    private Manager managerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(firstName, client.firstName) && Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, email);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", managerId=" + managerId +
                ", status=" + status +
                ", taxCode='" + taxCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}