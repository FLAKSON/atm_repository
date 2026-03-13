package org.maksymtiutiunnyk.atmproject.service;

import org.maksymtiutiunnyk.atmproject.entites.*;
import org.maksymtiutiunnyk.atmproject.repositories.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private static Session session;

    public void startOfSession(Card card, Atm atm, Account account) {
        session = Session.createSession(card, atm, account);
        saveSession(session);
    }

    public void authorizedSession() {
        session.authorizedSession();
        saveSession(session);
    }

    public void endOfSession() {
        session.closeSession();
        saveSession(session);
    }

    public void addTransactionInSession(Transaction transaction) {
        session.addTransaction(transaction);
        saveSession(session);
    }

    public void addFailed() {
        session.addFailedPinAttempts();
        saveSession(session);
    }

    private void saveSession(Session session) {
        sessionRepository.save(session);
    }


}
