package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entities.*;
import org.maksymtiutiunnyk.atmproject.enums.CardStatuses;
import org.maksymtiutiunnyk.atmproject.repositories.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final CardService cardService;

    public SessionService(SessionRepository sessionRepository, CardService cardService) {
        this.sessionRepository = sessionRepository;
        this.cardService = cardService;
    }

    private Session session;

    @Transactional
    public void startSession(String cardNumber, Atm atm) {
        Card card = cardService.getCard(cardNumber);
        Account account = card.getCustomer().getAccount();
        session = Session.start(card, atm, account);
        sessionRepository.save(session);
    }

    public void authorizedSession() {
        session.authorize();
        sessionRepository.save(session);
    }


    public void closeSession() {
        if (!session.isEnded()) {
            session.close();
            sessionRepository.save(session);
        }
    }

    public boolean isSessionAuthorized() {
        return session.isAuthorized();
    }

    public boolean isCardAvailable(String cardNumber) {
        Card card = cardService.getCard(cardNumber);
        return card.getStatus() == CardStatuses.ACTIVE;
    }

    public void addTransactionInSession(Transaction transaction) {
        session.addTransaction(transaction);
    }

    public void addFailed() {
        session.registerFailedPinAttempt();
        sessionRepository.save(session);
    }

    public boolean isSessionEnded() {
        return session.isEnded();
    }

}
