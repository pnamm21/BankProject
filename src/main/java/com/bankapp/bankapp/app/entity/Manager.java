package com.bankapp.bankapp.app.entity;

import com.bankapp.bankapp.app.entity.enums.ManagerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "managers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@NamedEntityGraph(name = "manager-client-account-graph",
//        attributeNodes = @NamedAttributeNode(value = "clients", subgraph = "client"),
//        subgraphs = {
//                @NamedSubgraph(name = "client", attributeNodes = {@NamedAttributeNode("status"),
//                        @NamedAttributeNode("createdAt"),
//                        @NamedAttributeNode(value = "accounts", subgraph = "account")}),
//                @NamedSubgraph(name = "account", attributeNodes = {@NamedAttributeNode(value = "agreements"),
//                        @NamedAttributeNode("transactions"),
//                        @NamedAttributeNode("status")})})
public class Manager {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ManagerStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany
    private List<Client> clients;

    @JsonIgnore
    @OneToMany
    private List<Product> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return Objects.equals(id, manager.id) && Objects.equals(firstName, manager.firstName) && Objects.equals(lastName, manager.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}