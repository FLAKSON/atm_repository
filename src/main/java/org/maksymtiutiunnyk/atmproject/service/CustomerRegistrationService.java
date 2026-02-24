package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entites.Account;
import org.maksymtiutiunnyk.atmproject.entites.Card;
import org.maksymtiutiunnyk.atmproject.entites.Customer;
import org.maksymtiutiunnyk.atmproject.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationService {
    private final CustomerRepository customerRepository;
    private final CardService cardService;
    public CustomerRegistrationService(CustomerRepository customerRepository, CardService cardService) {
        this.customerRepository = customerRepository;
        this.cardService = cardService;
    }

    @Transactional
    public void registerCustomer(String name, String surname, String passportId, String currency, String pin) {
        passportId = Customer.validatePassportFormat(passportId);
        if (customerRepository.existsByPassportId(passportId)) {
            throw new RuntimeException("Customer already exists");
        }
        Customer customer = Customer.createNewCustomer(name, surname, passportId);
        Account account = Account.createAccount(customer, currency);
        Card card = issueCard(passportId, customer, pin);
        customer.attachAccount(account);
        customer.addCard(card);
        customerRepository.save(customer);
    }

    @Transactional
    protected Card issueCard(String passportId, Customer customer, String pin) {
        String pan = cardService.createPan(passportId);
        String hash = cardService.createHashPassword(pin);
        return Card.createCard(pan, customer, hash);
    }
}

