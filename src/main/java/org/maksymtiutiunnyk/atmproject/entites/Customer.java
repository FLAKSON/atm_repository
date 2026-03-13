package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.CustomerStatuses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true,  updatable = false)
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
        customer.passportId = validatePassportFormat(passportId);
        customer.name = validateName(name);
        customer.surname = validateSurname(surname);
        customer.cards = new ArrayList<>();
        return customer;
    }

    public static String validatePassportFormat(String passportId) {
        Objects.requireNonNull(passportId, "PassportId is null");
        passportId = passportId.trim().toUpperCase();
        Pattern pattern = Pattern.compile("[A-Za-z]{2}\\d{4}");
        Matcher matcher = pattern.matcher(passportId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Passport Id is invalid");
        }
        return passportId;
    }

    public void attachAccount(Account account) {
        Objects.requireNonNull(account, "Account is null");
        this.account = account;
    }

    public static String validateName(String name) {
        return validator(name);
    }

    public static String validateSurname(String surname) {
        return validator(surname);
    }

    protected static String validator(String nameOrSurname) {
        String validateNameOrSurname = nameOrSurname.trim().toUpperCase();
        if (validateNameOrSurname.length() < 2 || validateNameOrSurname.length() > 20) {
            throw new IllegalArgumentException("Length must be between 2 and 20");
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(validateNameOrSurname);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Entered is not valid");
        }
        return validateNameOrSurname;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

}
