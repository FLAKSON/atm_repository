package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.maksymtiutiunnyk.atmproject.enums.CassetteStatuses;
import org.maksymtiutiunnyk.atmproject.enums.DenominationTypes;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cassettes")
public class Cassette {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Getter
    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DenominationTypes denomination;

    @Getter
    @Column(nullable = false)
    private int count;

    @Getter
    @Column(nullable = false, updatable = false)
    private static final int CAPACITY = 1000;

    @Getter
    @Enumerated(EnumType.STRING)
    private CassetteStatuses status = CassetteStatuses.OK;

    @Getter
    @Setter
    @JoinColumn(name = "atm_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Atm atm;

    public static Cassette createCassette(DenominationTypes denomination, int count) {
        Objects.requireNonNull(denomination, "Denomination is null");
        if (count > CAPACITY || count <= 0) {
            throw new IllegalArgumentException("Count must be between 0 and 1000");
        }
        Cassette cassette = new Cassette();
        cassette.denomination = denomination;
        cassette.count = count;
        return cassette;
    }

    @Override
    public String toString() {
        return "Cassette with " +
                "id: " + id +
                ", Denomination: " + denomination +
                ", Count Of Banknote: " + count +
                ", status: " + status;
    }

    public void updateCassette() {
        if (status == CassetteStatuses.OK && count != 1000) {
            count = 1000;
        }
        else {
            throw new IllegalStateException("Error updating Cassette");
        }
    }

    public void withdrawBill(int count) {
        this.count -= count;
    }
}
