package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entities.Account;
import org.maksymtiutiunnyk.atmproject.entities.Card;
import org.maksymtiutiunnyk.atmproject.repositories.AccountRepository;
import org.maksymtiutiunnyk.atmproject.repositories.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CardRepository cardRepository;

    public CustomerService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Account getUserByCardPan(String cardNumber) {
        Card card = cardRepository.findByPan(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        return card.getCustomer().getAccount();
    }

    @Transactional
    public void isAmountForWithdrawValid(String amount, Account account) {
        if (amount.isEmpty()) {
            throw new IllegalArgumentException("Amount is empty.");
        }
        long longAmount = Long.parseLong(amount);
        if (longAmount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (account.getAvailableBalance() < longAmount) {
            throw new IllegalArgumentException("Lack of balance.");
        }
        if (account.getDailyWithdrawLimit() < longAmount && account.getDailyWithdrawLimit() - account.getDailyWithdrawLimitUsed() < longAmount) {
            throw new IllegalArgumentException("Lack of withdraw limit.");
        }
    }
}
