package org.maksymtiutiunnyk.atmproject.service;

import org.maksymtiutiunnyk.atmproject.entites.Account;
import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.entites.Card;
import org.maksymtiutiunnyk.atmproject.entites.Customer;
import org.maksymtiutiunnyk.atmproject.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationService {
    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;
    private final CardService cardService;

    public CustomerRegistrationService(CustomerRepository customerRepository, AuditLogService auditLogService, CardService cardService) {
        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
        this.cardService = cardService;
    }

    public void registerCustomer(String name, String surname, String passportId, String currency, String pin, Atm atm) {
        auditLogService.registrationStarted(atm);
        try {
            passportId = Customer.validatePassportFormat(passportId);
            if (customerRepository.existsByPassportId(passportId)) {
                throw new IllegalArgumentException("Customer with this passport id already exists");
            }
            Customer customer = Customer.createNewCustomer(name, surname, passportId);
            Account account = Account.createAccount(customer, currency);
            Card card = cardService.createCard(passportId, customer, pin);
            customer.attachAccount(account);
            customer.addCard(card);
            customerRepository.save(customer);
            auditLogService.registrationSuccessful(atm, card);
        } catch (Exception e) {
            auditLogService.registrationFailed(atm, e);
            throw e;
        }
    }
}
