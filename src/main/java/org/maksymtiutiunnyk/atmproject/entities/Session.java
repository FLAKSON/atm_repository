package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.SessionStatuses;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Getter
    private LocalDateTime authorizedAt;

    @Getter
    private LocalDateTime endedAt;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatuses status;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_account_id")
    private Account account;

    @Getter
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public static Session start(Card card, Atm atm, Account account) {
        Objects.requireNonNull(card, "Card can't be null");
        Objects.requireNonNull(atm, "Atm can't be null");
        Objects.requireNonNull(account, "Account can't be null");
        Session session = new Session();
        session.card = card;
        session.atm = atm;
        session.account = account;
        session.startedAt = LocalDateTime.now();
        session.status = SessionStatuses.UNAUTHORIZED;
        return session;
    }

    public void authorize() {
        ensureNotEnded();
        if (this.status != SessionStatuses.UNAUTHORIZED) {
            throw new IllegalStateException("Session is not waiting for authorization.");
        }
        this.status = SessionStatuses.AUTHORIZED;
        this.authorizedAt = LocalDateTime.now();
    }

    public void close() {
        if (status == SessionStatuses.ENDED) {
            return;
        }
        this.status = SessionStatuses.ENDED;
        this.endedAt = LocalDateTime.now();
    }

    public void addTransaction(Transaction transaction) {
        ensureNotEnded();
        Objects.requireNonNull(transaction, "Transaction can't be null");
        if (this.status != SessionStatuses.AUTHORIZED) {
            throw new IllegalStateException("Adding transactions allowed only for authorized session.");
        }
        this.transactions.add(transaction);
        transaction.addSession(this);
    }

    public boolean isEnded() {
        return status == SessionStatuses.ENDED;
    }

    public void registerFailedPinAttempt() {
        ensureNotEnded();
        if (this.status != SessionStatuses.UNAUTHORIZED) {
            throw new IllegalStateException("Cannot register failed pin attempts for authorized session.");
        }
        this.failedPinAttempts++;
        if (this.failedPinAttempts >= maxPinAttempts) {
            close();
        }
    }

    public boolean isAuthorized() {
        return status == SessionStatuses.AUTHORIZED;
    }

    private void ensureNotEnded() {
        if (this.status == SessionStatuses.ENDED) {
            throw new IllegalStateException("Session has been closed.");
        }
    }
}
