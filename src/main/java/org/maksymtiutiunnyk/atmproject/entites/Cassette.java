package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.CassetteStatuses;

@Entity
@NoArgsConstructor
@Table(name = "cassettes")
public class Cassette {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Getter
    @Column(nullable = false, updatable = false)
    private int denomination;

    @Getter
    @Column(nullable = false)
    private int count;

    @Getter
    @Column(nullable = false, updatable = false)
    private int maxCapacity;

    @Getter
    @Enumerated(EnumType.STRING)
    private CassetteStatuses status = CassetteStatuses.OK;

    @Getter
    @JoinColumn(name = "atm_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Atm atm;
}
