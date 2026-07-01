package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.maksymtiutiunnyk.atmproject.enums.TransactionStatuses;
import org.maksymtiutiunnyk.atmproject.enums.TransactionTypes;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "transactions")
public class Transaction {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true,  updatable = false)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionTypes type;

    @Getter
    @Column(nullable = false)
    private String currency;

    @Getter
    @Column(nullable = false)
    private long amount;

    @Getter
    @Enumerated(EnumType.STRING)
    private TransactionStatuses status = TransactionStatuses.PENDING;

    @Getter
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime endedAt;

    @Getter
    @JoinColumn(name = "atm_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Atm atm;

    @Getter
    @JoinColumn(name = "card_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

    @Getter
    @JoinColumn(name = "account_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Getter
    @JoinColumn(name = "target_account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account targetAccount;

    @Getter
    @Column(length = 1024)
    private String errorReason;

    @Getter
    @JoinColumn(name = "session_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    public static Transaction createPending(
            TransactionTypes transactionType,
            long amount,
            Atm atm,
            Card card,
            Account account,
            Account targetAccount
    ) {
        Objects.requireNonNull(transactionType, "Transaction type cannot be null");
        Objects.requireNonNull(atm, "Atm cannot be null");
        Objects.requireNonNull(card, "Card cannot be null");
        Objects.requireNonNull(account, "Account cannot be null");
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount is negative or zero");
        }
        if (transactionType == TransactionTypes.TRANSFER && targetAccount == null) {
            throw new IllegalArgumentException("Target account cannot null");
        }
        Transaction transaction = new Transaction();
        transaction.type = transactionType;
        transaction.amount = amount;
        transaction.atm = atm;
        transaction.card = card;
        transaction.account = account;
        transaction.targetAccount = targetAccount;
        return transaction;
    }
    public void ensurePending() {
        if (this.status != TransactionStatuses.PENDING) {
            throw new IllegalStateException("Transaction must be pending.");
        }
    }

    public void approve() {
        ensurePending();
        this.status = TransactionStatuses.APPROVED;
        this.errorReason = null;
        this.endedAt = LocalDateTime.now();
    }

    public void decline(String reason) {
        ensurePending();
        this.status = TransactionStatuses.DECLINED;
        this.errorReason = normalizeErrorReason(reason);
        this.endedAt = LocalDateTime.now();
    }

    public void reverse(String reason) {
        if (this.status != TransactionStatuses.APPROVED && this.status != TransactionStatuses.PENDING) {
            throw new IllegalStateException("Transaction must be approved or pending");
        }
        this.status = TransactionStatuses.REVERSED;
        this.errorReason = normalizeErrorReason(reason);
        this.endedAt = LocalDateTime.now();
    }

    public String normalizeErrorReason(String reason) {
        if (reason == null || reason.isEmpty()) {
            return "UNKNOWN";
        }
        return reason.trim();
    }

    public void addSession(Session session) {
        Objects.requireNonNull(session, "Session cannot be null");
        this.session = session;
    }
}


