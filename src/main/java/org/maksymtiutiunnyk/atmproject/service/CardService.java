package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entites.Account;
import org.maksymtiutiunnyk.atmproject.entites.Card;
import org.maksymtiutiunnyk.atmproject.repositories.CardRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardService {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public String createPan(String passportId) {
        if (passportId == null || passportId.length() < 6) {
            throw new RuntimeException("Passport ID is null and must have at least 6 characters");
        }
        char firstChar = Character.toUpperCase(passportId.charAt(0));
        char secondChar = Character.toUpperCase(passportId.charAt(1));
        if (firstChar < 'A' || firstChar > 'Z' || secondChar < 'A' || secondChar > 'Z') {
            throw new RuntimeException("First two characters must be between A and Z");
        }
        int v1 =  firstChar - '0';
        int v2 = secondChar - '0';
        return new StringBuilder(passportId.length() + 2)
                .append(v1)
                .append(v2)
                .append(passportId, 2, passportId.length())
                .toString();
    }

    @Transactional
    public String createHashPassword(String password) {
        String normalizedPassword = password.trim();
        if (normalizedPassword.isEmpty() || normalizedPassword.length() > 4) {
            throw new RuntimeException("Password is null and must have at least 4 characters");
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(normalizedPassword);
        if (matcher.matches()) {
            throw new RuntimeException("Password contains invalid characters");
        }
        return hashPassword(normalizedPassword);
    }

    @Transactional
    protected String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    protected String validateEnteredPan(String pan) {
        if (pan == null) {
            throw  new IllegalArgumentException("Pan is null!");
        }
        String normalizedPan = pan.trim();
        if (normalizedPan.isEmpty()) {
            throw new IllegalArgumentException("Pan is empty!");
        }
        final Pattern pattern = Pattern.compile("\\d{8}");
        final Matcher matcher = pattern.matcher(normalizedPan);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pan is invalid!");
        }
        return normalizedPan;
    }

    protected String validateEnteredPin(String pin) {
        if (pin == null) {
            throw  new IllegalArgumentException("Pin is null!");
        }
        String normalizedPin = pin.trim();
        if (normalizedPin.isEmpty()) {
            throw new IllegalArgumentException("Pin is empty!");
        }
        final Pattern pattern = Pattern.compile("\\d{4}");
        final Matcher matcher = pattern.matcher(normalizedPin);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pin is invalid!");
        }
        return normalizedPin;
    }

    protected final boolean passwordVerification(String enteredPin, String hashedPassword) {
        return passwordEncoder.matches(enteredPin, hashedPassword);
    }

    public void checkPan(String pan) {
        try {
            getCard(pan);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage() + pan);
        }
    }

    @Transactional
    public Account getAccessToAccountFromCard(String enteredPan, String enteredPin) {
        enteredPan = validateEnteredPan(enteredPan);
        enteredPin = validateEnteredPin(enteredPin);
        getCard(enteredPan);
        Card card = getCard(enteredPan);
        String pinHash = getPinHash(card);
        if (passwordVerification(enteredPin, pinHash)) {
            return getAccountFromCard(card);
        }
        throw new IllegalArgumentException("Pin is invalid!");
    }

    protected final Account getAccountFromCard(Card card) {
        return card.getCustomer().getAccount();
    }

    protected final String getPinHash(Card card) {
        return card.getPinHash();
    }

    protected final Card getCard(String pan) {
        return cardRepository.findByPan(pan).orElseThrow
                (() -> new IllegalArgumentException("Card with this pan not found: ")
        );
    }
}
