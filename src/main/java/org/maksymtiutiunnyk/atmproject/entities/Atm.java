package org.maksymtiutiunnyk.atmproject.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.AtmStatuses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @OneToMany(mappedBy = "atm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Cassette> cassettes = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "atm", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public static Atm createAtm(String location) {
        Objects.requireNonNull(location, "Location cannot be null");
        if (location.length() <= 2) {
            throw new IllegalArgumentException("Location must have at least 2 characters");
        }
        Atm atm = new Atm();
        atm.location = location;
        return atm;
    }

    public void setAtmStatus(AtmStatuses atmStatus) {
        this.status = atmStatus;
    }

    public void addCassette(Cassette cassette) {
        if (cassettes.size() >= 6) {
            throw new IllegalStateException("Maximum number of Cassettes is 6.");
        }
        Objects.requireNonNull(cassette, "Cassette cannot be null");
        cassettes.add(cassette);
        cassette.setAtm(this);
    }

    @Override
    public String toString() {
        return "Atm" +
                "id: " + id +
                "location: " + location +
                "status:" + status;
    }
}
