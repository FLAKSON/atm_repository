package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.maksymtiutiunnyk.atmproject.enums.TransactionStatuses;
import org.maksymtiutiunnyk.atmproject.enums.TransactionTypes;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
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
    @Column(nullable = false)
    private LocalDateTime finalizedAt;

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
            Account targetAccount,
            Session session
    ) {
        Objects.requireNonNull(transactionType, "transactionType is null");
        Objects.requireNonNull(atm, "atm is null");
        Objects.requireNonNull(card, "card is null");
        Objects.requireNonNull(account, "account is null");
        Objects.requireNonNull(session, "session is null");

        if (amount <= 0) {
            throw new IllegalArgumentException("amount is negative");
        }
        if (transactionType == TransactionTypes.TRANSFER && targetAccount == null) {
            throw new IllegalArgumentException("targetAccount is null");
        }
        Transaction transaction = new Transaction();
        transaction.type = transactionType;
        transaction.amount = amount;
        transaction.createdAt = LocalDateTime.now();
        transaction.atm = atm;
        transaction.card = card;
        transaction.account = account;
        transaction.targetAccount = targetAccount;
        transaction.session = session;
        return transaction;
    }
    public void ensurePending() {
        if (this.status != TransactionStatuses.PENDING) {
            throw new IllegalStateException("Transaction must be PENDING, Current status: " + status);
        }
    }

    public void approved() {
        ensurePending();
        this.status = TransactionStatuses.APPROVED;
        this.errorReason = null;
        this.finalizedAt = LocalDateTime.now();
    }

    public void decline(String reason) {
        ensurePending();
        this.status = TransactionStatuses.DECLINED;
        this.errorReason = normalizeErrorReason(reason);
        this.finalizedAt = LocalDateTime.now();
    }

    public void reverse(String reason) {
        if (this.status != TransactionStatuses.APPROVED && this.status != TransactionStatuses.PENDING) {
            throw new IllegalStateException("Transaction must be APPROVED or PENDING, Current status: " + status);
        }
        this.status = TransactionStatuses.REVERSED;
        this.errorReason = normalizeErrorReason(reason);
        this.finalizedAt = LocalDateTime.now();
    }

    public String normalizeErrorReason(String reason) {
        if (reason == null || reason.isEmpty()) {
            return "UNKNOW";
        }
        return reason.trim();
    }

    public void addSession(Session session) {
        this.session = session;
    }
}


