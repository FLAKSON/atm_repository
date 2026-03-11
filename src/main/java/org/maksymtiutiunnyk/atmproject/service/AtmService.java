package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entites.Account;
import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.entites.Card;
import org.maksymtiutiunnyk.atmproject.entites.Customer;
import org.maksymtiutiunnyk.atmproject.enums.AtmStatuses;
import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;
import org.maksymtiutiunnyk.atmproject.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class AtmService {
    private final AtmRepository atmRepository;
    private final CardService cardService;
    private final AuditLogService auditLogService;
    private final CustomerRepository customerRepository;
    public AtmService
            (AtmRepository atmRepository,
            CardService cardService,
             AuditLogService auditLogService,
             CustomerRepository customerRepository
            )
    {
        this.atmRepository = atmRepository;
        this.cardService = cardService;
        this.auditLogService = auditLogService;
        this.customerRepository = customerRepository;
    }
    @Transactional
    public Atm connectToAtm(Long atmId, String location) {
        if (atmRepository.findById(atmId).isEmpty()) {
            createAtm(location);
        }
        return atmRepository.findById(atmId).get();
    }
    @Transactional
    protected void createAtm(String location) {
        atmRepository.save(Atm.createAtm(location));
    }

    public void changeAtmStatus(Atm atm, AtmStatuses atmStatus) {
        atm.setAtmStatus(atmStatus);
        atmRepository.save(atm);
    }

    public Account checkBalance(String pan, String pin) {
        return cardService.getAccessToAccountFromCard(pan, pin);
    }

    public void closeInteraction(Atm atm) {
        changeAtmStatus(atm, AtmStatuses.AWAITING);
    }

    @Transactional
    public void registerCustomer(String name, String surname, String passportId, String currency, String pin, Atm atm) {
        try {
            auditLogService.registrationStarted(atm);
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
            auditLogService.registrationSuccessful(atm);
        } catch (Exception e) {
            auditLogService.registrationFailed(atm, e);
            throw e;
        }
    }

    @Transactional
    protected Card issueCard(String passportId, Customer customer, String pin) {
        String pan = cardService.createPan(passportId);
        String hash = cardService.createHashPassword(pin);
        return Card.createCard(pan, customer, hash);
    }

}
