package org.maksymtiutiunnyk.atmproject.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.maksymtiutiunnyk.atmproject.enums.DenominationTypes;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InputValidator {
    private final static Pattern PIN_PATTERN = Pattern.compile("\\d{4}");
    private final static Pattern PAN_PATTERN = Pattern.compile("\\d{8}");
    private final static Pattern NAME_PATTERN = Pattern.compile("^[A-Z]+$");
    private final static Pattern PASSPORT_PATTERN = Pattern.compile("[A-Z]{2}\\d{4}");

    public static String validatePin(String pin) {
        Objects.requireNonNull(pin, "Pin cannot be null");
        String normalizedPin = normalize(pin);
        Matcher matcher = PIN_PATTERN.matcher(normalizedPin);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pin is invalid");
        }
        return normalizedPin;
    }

    public static String validatePan(String pan) {
        Objects.requireNonNull(pan, "Pan cannot be null");
        String normalizedPan = normalize(pan);
        Matcher matcher = PAN_PATTERN.matcher(normalizedPan);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Pan must contains 8 characters! ");
        }
        return normalizedPan;
    }

    public static String validateName(String name) {
        return nameOrSurnameValidator(name);
    }

    public static String validateSurname(String surname) {
        return nameOrSurnameValidator(surname);
    }

    public static String validatePassportId(String passportId) {
        return passportValidator(passportId);
    }

    public static DenominationTypes validateDenomination(String denomination) {
        return denominationValidator(denomination);
    }

    private static String normalize(String data) {
        return data.trim();
    }

    private static DenominationTypes denominationValidator(String denomination) {
        return switch (denomination) {
            case "1" -> DenominationTypes.FIVE;
            case "2" -> DenominationTypes.TEN;
            case "3" -> DenominationTypes.TWENTY;
            case "4" -> DenominationTypes.FIFTY;
            default -> throw new IllegalArgumentException("You need to select correct denomination!");
        };
    }


    private static String nameOrSurnameValidator(String nameOrSurname) {
        Objects.requireNonNull(nameOrSurname, "Name or Surname cannot be null");
        String validatedNameOrSurname = nameOrSurname.trim().toUpperCase();
        if (validatedNameOrSurname.length() < 2 || validatedNameOrSurname.length() > 20) {
            throw new IllegalArgumentException("Length must be between 2 and 20");
        }
        Matcher matcher = NAME_PATTERN.matcher(validatedNameOrSurname);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Entered is not valid");
        }
        return validatedNameOrSurname;
    }

    private static String passportValidator(String passportId) {
        Objects.requireNonNull(passportId, "Passport id cannot be null");
        passportId = passportId.trim().toUpperCase();
        Matcher matcher = PASSPORT_PATTERN.matcher(passportId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Passport Id is invalid");
        }
        return passportId;
    }

}
