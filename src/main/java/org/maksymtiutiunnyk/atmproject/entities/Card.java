package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.CardStatuses;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        card.pan = pan;
        card.customer = customer;
        card.pinHash = pinHash;
        card.expirationDate = LocalDate.now().plusYears(4);
        return card;
    }
}
