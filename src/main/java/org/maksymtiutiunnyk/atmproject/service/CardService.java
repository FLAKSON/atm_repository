package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardService {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        if (normalizedPassword == null || normalizedPassword.length() > 4) {
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
    public boolean decryptPassword(String hashedPassword, String enteredPassword) {
        if (passwordEncoder.matches(hashedPassword, enteredPassword)) {
            return true;
        }
        throw new RuntimeException("Password does not match");
    }

    @Transactional
    protected String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
