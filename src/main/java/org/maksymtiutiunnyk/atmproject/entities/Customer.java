package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.CustomerStatuses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "customers")
public class Customer {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,  updatable = false)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(nullable = false)
    private String surname;

    @Getter
    @Column(nullable = false, unique = true)
    private String passportId;

    @Getter
    @Enumerated(EnumType.STRING)
    private CustomerStatuses status = CustomerStatuses.ACTIVE;

    @Getter
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Card> cards;

    @Getter
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    public static Customer createNewCustomer(String name, String surname, String passportId) {
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(surname, "surname is null");
        Objects.requireNonNull(passportId, "passportId is null");
        Customer customer = new Customer();
        customer.passportId = passportId;
        customer.name = name;
        customer.surname = surname;
        customer.cards = new ArrayList<>();
        return customer;
    }

    public void attachAccount(Account account) {
        Objects.requireNonNull(account, "Account is null");
        if (this.account != null) {
            throw  new IllegalStateException("Account already exists");
        }
        this.account = account;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

}
