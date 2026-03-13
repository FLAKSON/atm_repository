package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.AtmStatuses;

import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "atms")
public class Atm {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String location;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AtmStatuses status = AtmStatuses.AWAITING;

    @Getter
    @OneToMany(mappedBy = "atm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cassette> cassettes;

    @Getter
    @OneToMany(mappedBy = "atm", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public static Atm createAtm(String location) {
        if (location == null || location.length() < 2) {
            throw new IllegalArgumentException("location must have at least 2 characters");
        }
        Atm atm = new Atm();
        atm.location = location;
        return atm;
    }

    public void setAtmStatus(AtmStatuses atmStatus) {
        this.status = atmStatus;
    }
}
