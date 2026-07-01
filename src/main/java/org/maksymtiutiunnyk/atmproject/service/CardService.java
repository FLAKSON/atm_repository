package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entities.Account;
import org.maksymtiutiunnyk.atmproject.entities.Card;
import org.maksymtiutiunnyk.atmproject.entities.Customer;
import org.maksymtiutiunnyk.atmproject.repositories.CardRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardService {
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final Pattern PAN_PATTERN = Pattern.compile("\\d{8}");
    private static final Pattern PIN_PATTERN = Pattern.compile("\\d{4}");

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /*This method creates pan mask from user's passport ID.*/
    private String createPan(String passportId) {
        if (passportId == null) {
            throw new IllegalArgumentException("Passport ID can't be null.");
        }
        if (passportId.length() < 6) {
            throw new IllegalArgumentException("Passport ID must have at least 6 characters.");
        }
        char firstChar = Character.toUpperCase(passportId.charAt(0));
        char secondChar = Character.toUpperCase(passportId.charAt(1));
        if (firstChar < 'A' || firstChar > 'Z' || secondChar < 'A' || secondChar > 'Z') {
            throw new IllegalArgumentException("First two characters must be between A and Z.");
        }
        int v1 =  firstChar - '0';
        int v2 = secondChar - '0';
        return String.valueOf(v1) + v2 + passportId.substring(2);
    }

    public String getPanToViewUser(String passportId) {
        return createPan(passportId);
    }

    /*A method that gets an account after checking the pin and card*/
    @Transactional
    public Account getAccessToAccount(String pan, String pin) {
            String normalizedPan = validatePan(pan);
            String normalizedPin = validatePin(pin);
            Card card = getCardByPan(normalizedPan);
            if (!verifyCardAccess(card, normalizedPin)) {
                throw new IllegalArgumentException("Invalid pan or pin provided.");
            }
            return getAccountByCard(card);
    }

    @Transactional
    public Account getAccessToAccount(String pan, boolean isAuthorized) {
        if (!isAuthorized) {
            throw new IllegalArgumentException("Access to account is not authorized.");
        }
        else {
            return getAccountByCard(getCardByPan(pan));
        }
    }

    private boolean verifyCardAccess(Card card, String pin) {
        return PASSWORD_ENCODER.matches(pin, card.getPinHash());
    }

    private Card getCardByPan(String pan) {
        return cardRepository.findByPan(pan).orElseThrow(() -> new IllegalArgumentException("Card with this pan not found."));
    }

    private static Account getAccountByCard(Card card) {
        return card.getCustomer().getAccount();
    }

    private String validatePan(String pan) {
        if (pan == null) {
            throw  new IllegalArgumentException("Pan is null!");
        }
        String normalizedPan = pan.trim();
        if (normalizedPan.isEmpty()) {
            throw new IllegalArgumentException("Pan is empty!");
        }
        if (!PAN_PATTERN.matcher(normalizedPan).matches()) {
            throw new IllegalArgumentException("Pan is invalid!");
        }
        return normalizedPan;
    }

    private String validatePin(String pin) {
        Objects.requireNonNull(Objects.requireNonNull(pin, "Pin is null!"));
        String normalizedPin = pin.trim();
        if (normalizedPin.isEmpty()) {
            throw new IllegalArgumentException("Pin is empty!");
        }
        if (!PIN_PATTERN.matcher(normalizedPin).matches()) {
            throw new IllegalArgumentException("Pin is invalid!");
        }
        return normalizedPin;
    }

    public void checkPan(String pan) {
        getCardByPan(pan);
    }

    public Card getCard(String pan) {
        return getCardByPan(pan);
    }

    /*A method that create a card.*/
    public Card createCard(String passportId, Customer customer, String pin) {
        return Card.createCard(createPan(passportId), customer, createHashedPin(pin));
    }

    /*A method that create hashed pin for card.*/
    private String createHashedPin(String password) {
        String normalizedPassword = password.trim();
        if (normalizedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password is null.");
        }
        if (normalizedPassword.length() > 4) {
            throw new IllegalArgumentException("Password must have 4 characters.");
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(normalizedPassword);
        if (matcher.matches()) {
            throw new IllegalArgumentException("Password contains invalid characters.");
        }
        return hashPassword(normalizedPassword);
    }

    private String hashPassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

}
