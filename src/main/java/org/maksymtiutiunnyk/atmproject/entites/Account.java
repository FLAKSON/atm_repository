package org.maksymtiutiunnyk.atmproject.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false, unique = true)
    private Customer customer;

    @Getter
    @Column(nullable = false, updatable = false)
    private String currency;

    @Getter
    @Column(nullable = false)
    private long availableBalance = 0;

    @Getter
    private long dailyWithdrawLimit = 1000;

    @Getter
    private long dailyWithdrawLimitUsed = 0;

    @Getter
    private long dailyTransferLimit = 2000;

    @Getter
    private long dailyTransferLimitUsed = 0;

    @Getter
    private long perTransactionLimit = 250;

    @Getter
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

     public static Account createAccount(Customer customer, String currency) {
         Objects.requireNonNull(customer, "customer is null");
         Objects.requireNonNull(currency, "currency is null");

         Account account = new Account();
         account.customer = customer;
         account.currency = currency;
         return account;
     }
}
