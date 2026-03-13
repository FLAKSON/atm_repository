package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.maksymtiutiunnyk.atmproject.enums.SessionStatuses;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class Session {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Getter
    @JoinColumn(name = "card_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

    @Getter
    @JoinColumn(name = "atm_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Atm atm;

    @Getter
    private int failedPinAttempts = 0;

    @Getter
    private int maxPinAttempts = 3;

    @Getter
    @CreationTimestamp
    private LocalDateTime startTime;

    @Getter
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Getter
    @Enumerated(EnumType.STRING)
    private SessionStatuses status = SessionStatuses.UNAUTHORIZED;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_account_id")
    private Account account;

    @Getter
    private int timeOutSeconds = 15;

    @Getter
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public static Session createSession(Card card, Atm atm, Account account) {
        Objects.requireNonNull(card, "Card can't be null");
        Objects.requireNonNull(atm, "Atm can't be null");
        Objects.requireNonNull(account, "Account can't be null");
        Session session = new Session();
        session.card = card;
        session.atm = atm;
        session.account = account;
        return session;
    }

    public void addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction can't be null");
        transactions.add(transaction);
        transaction.addSession(this);
    }

    public void closeSession() {
        this.endTime = LocalDateTime.now();
        this.status = SessionStatuses.ENDED;
    }

    public void authorizedSession() {
        this.status = SessionStatuses.AUTHORIZED;
    }

    public void addFailedPinAttempts() {
        this.failedPinAttempts++;
    }
}
