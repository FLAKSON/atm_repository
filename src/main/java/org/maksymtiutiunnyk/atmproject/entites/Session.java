package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.maksymtiutiunnyk.atmproject.enums.SessionStatuses;
import java.time.LocalDateTime;

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
    private int failedPinAttempts = 3;

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
    private Account selectedAccount;

    @Getter
    private int timeOutSeconds = 15;
}
