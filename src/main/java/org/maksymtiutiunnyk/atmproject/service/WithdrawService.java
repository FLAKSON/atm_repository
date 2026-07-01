package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entities.Account;
import org.maksymtiutiunnyk.atmproject.entities.Atm;
import org.maksymtiutiunnyk.atmproject.entities.Cassette;
import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;
import org.maksymtiutiunnyk.atmproject.repositories.CardRepository;
import org.maksymtiutiunnyk.atmproject.repositories.CassetteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WithdrawService {

    private final AtmRepository atmRepository;
    private final CassetteRepository cassetteRepository;
    private final CustomerService customerService;


    public WithdrawService(AtmRepository atmRepository, CassetteRepository cassetteRepository, CustomerService customerService) {
        this.atmRepository = atmRepository;
        this.cassetteRepository = cassetteRepository;
        this.customerService = customerService;
    }

    @Transactional
    protected Map<Long, Integer> makeWithdrawPlan(String atmId, String amount) {
        Long withdrawAmount = Long.parseLong(amount);
        isAmountValidForDeposit(withdrawAmount);
        long remainingAmount = withdrawAmount;
        Atm atm = atmRepository.getAtmById(Long.parseLong(atmId));
        List<Cassette> cassettes = atm.getCassettes();

        Map<Long, Integer> withdrawPlans = new HashMap<>();

        List<Cassette> sortedCassettes = cassettes.stream()
                .sorted(Comparator.comparingLong((Cassette cassette) -> cassette.getDenomination().getValue()))
                .toList()
                .reversed();

        for (Cassette cassette : sortedCassettes) {
            int countOfUsedBanknote = 0;
            while (true) {
                if (countOfUsedBanknote >= cassette.getCount()) {
                    break;
                }
                else if (cassette.getDenomination().getValue() > remainingAmount) {
                    break;
                }
                else {
                    remainingAmount -= cassette.getDenomination().getValue();
                    countOfUsedBanknote++;
                    if (remainingAmount == 0) {
                        break;
                    }
                }
            }
            if (countOfUsedBanknote != 0) {
                withdrawPlans.put(cassette.getId(), countOfUsedBanknote);
            }
            if (remainingAmount == 0) {
                return withdrawPlans;
            }
        }
        throw new RuntimeException("Not enough banknotes");
    }

    private void withdraw(Map<Long, Integer> withdrawPlan) {
        withdrawPlan.forEach((cassetteId, count) -> {
            cassetteRepository.getCassetteById(cassetteId).ifPresent(cassette -> {cassette.withdrawBill(count);});
        });
    }

    @Transactional
    public void makeWithdraw(String atmId, String amount, String cardNumber) {
        Account account = customerService.getUserByCardPan(cardNumber);
        isAccountValidForWithdraw(amount, cardNumber);
        customerService.isAmountForWithdrawValid(amount, account);
        Map<Long, Integer> plan =  makeWithdrawPlan(atmId, amount);
        withdraw(plan);
        account.withdraw(Long.parseLong(amount));
    }

    @Transactional
    protected void isAccountValidForWithdraw(String amount, String cardNumber) {
        long withdrawAmount = Long.parseLong(amount);
        Account account = customerService.getUserByCardPan(cardNumber);
        long dailyWithdrawLimit = account.getDailyWithdrawLimit();
        long availableBalance = account.getAvailableBalance();
        long dailyLimitUsed = account.getDailyWithdrawLimitUsed();
        if (withdrawAmount <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        if (withdrawAmount >= availableBalance) {
            throw new RuntimeException("Amount must be greater than available balance");
        }
        if (withdrawAmount >= (dailyWithdrawLimit -  dailyLimitUsed)) {
            throw new RuntimeException("Amount must be greater than daily withdraw limit used");
        }
    }



    @Transactional
    protected void isAmountValidForDeposit(Long amount) {
        if (amount % 5 != 0) {
            throw new IllegalArgumentException(amount + " - The amount must be a multiple of 5");
        }
    }


}
