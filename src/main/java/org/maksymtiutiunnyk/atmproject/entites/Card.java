package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.CardStatuses;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
@Table(name = "cards")
public class Card {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Getter
    @Column(unique = true, nullable = false, updatable = false)
    private String pan;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatuses status = CardStatuses.ACTIVE;

    @Getter
    @Column(nullable = false)
    private String pinHash;

    @Getter
    @Column(nullable = false)
    private LocalDate expirationDate;

    @Getter
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public static Card createCard(String pan, Customer customer, String pinHash) {
        Objects.requireNonNull(pan, "Pan is null");
        Objects.requireNonNull(customer, "Customer is null");
        Objects.requireNonNull(pinHash, "PinHash is null");
        Card card = new Card();
        try {
            validatePan(pan);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        card.pan = pan;
        card.customer = customer;
        card.pinHash = pinHash;
        card.expirationDate = LocalDate.now().plusYears(4);
        return card;
    }

    protected static String validatePan(String pan) {
        String normalizedPan = pan.trim();
        if (normalizedPan.isEmpty()) {
            throw new IllegalArgumentException("Pan is null or empty");
        }
        if (normalizedPan.length() != 8) {
            throw new IllegalArgumentException("Pan length is not 8 characters");
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(normalizedPan);
        if (matcher.matches()) {
            throw new IllegalArgumentException("Pan contains invalid characters");
        }
        return normalizedPan;
    }

    public static String validatePin(String pin) {
        Objects.requireNonNull(pin, "Pin is null");
        String  normalizedPin = pin.trim();
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(pin);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pin is invalid");
        }
        return normalizedPin;
    }

    public static String validateEnteredPan(String pan) {
        pan = pan.trim();
        Pattern pattern = Pattern.compile("\\d{8}");
        Matcher matcher = pattern.matcher(pan);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pan must contains 8 characters! ");
        }
        return pan;
    }
}
