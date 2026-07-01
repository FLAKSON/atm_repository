package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.maksymtiutiunnyk.atmproject.enums.AuditLogActorsType;
import org.maksymtiutiunnyk.atmproject.enums.AuditLogStatuses;
import org.maksymtiutiunnyk.atmproject.enums.AuditSeverity;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true,  updatable = false)
    private Long id;

    @Getter
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Getter
    @Enumerated(EnumType.STRING)
    private AuditLogStatuses auditLogStatuses;

    @Getter
    @Enumerated(EnumType.STRING)
    private AuditLogActorsType auditLogActorsType;

    @Getter
    @Enumerated(EnumType.STRING)
    private AuditSeverity auditLogSeverity;

    @Getter
    @Column(nullable = false)
    private String message;

    @Getter
    @JoinColumn(name = "session_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @Getter
    @JoinColumn(name = "atm_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Atm atm;

    @Getter
    @JoinColumn(name = "card_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Card card;

    @Getter
    @JoinColumn(name = "transaction_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Transaction transaction;

    public static AuditLog createAuditLog(AuditLogStatuses auditLogStatus, AuditLogActorsType auditLogActorType, AuditSeverity auditSeverity, String message, Session session, Atm atm, Card card, Transaction transaction) {
        Objects.requireNonNull(auditLogStatus, "auditLogStatus is null");
        Objects.requireNonNull(auditLogActorType, "auditLogActorType is null");
        Objects.requireNonNull(auditSeverity, "auditSeverity is null");
        Objects.requireNonNull(message, "message is null");
        Objects.requireNonNull(atm, "atm is null");
        AuditLog auditLog = new AuditLog();
        auditLog.auditLogStatuses = auditLogStatus;
        auditLog.auditLogActorsType = auditLogActorType;
        auditLog.auditLogSeverity = auditSeverity;
        auditLog.message = message;
        auditLog.session = session;
        auditLog.atm = atm;
        auditLog.card = card;
        auditLog.transaction = transaction;
        return auditLog;
    }

}
