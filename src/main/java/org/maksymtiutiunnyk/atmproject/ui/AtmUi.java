package org.maksymtiutiunnyk.atmproject.ui;

import org.maksymtiutiunnyk.atmproject.dto.AccountRegistrationDto;
import org.maksymtiutiunnyk.atmproject.entities.Atm;
import org.maksymtiutiunnyk.atmproject.entities.Cassette;
import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;
import org.maksymtiutiunnyk.atmproject.service.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AtmUi {
    private final ConsoleUi consoleUi;
    private final AtmRepository atmRepository;

    public AtmUi(ConsoleUi consoleUi, AtmRepository atmRepository) {
        this.consoleUi = consoleUi;
        this.atmRepository = atmRepository;
    }

    public String mainMenu() {
        consoleUi.divideScreen();
        consoleUi.println("Welcome to the Customer Menu!");
        consoleUi.println("1. Register (Register new account)");
        consoleUi.println("2. Insert Card (For control your balance or transaction)");
        consoleUi.println("0. Cancel (Exit)");
        return consoleUi.readNonBlank("Enter your choice: ");
    }

    public String withdrawMenu() {
        consoleUi.divideScreen();
        consoleUi.println("Welcome to the Withdraw Menu!");
        consoleUi.println("0. Exit from withdraw menu");
        return consoleUi.readNonBlank("Enter withdraw amount: ");
    }

    public String adminMenu() {
        consoleUi.divideScreen();
        consoleUi.println("Welcome to the Admin Menu!");
        consoleUi.println("1. Add cassette");
        consoleUi.println("2. Update cassette");
        consoleUi.println("3. Pull out cassette");
        consoleUi.println("4. Show list of cassette");
        consoleUi.println("0. Turn off admin menu");
        return consoleUi.readNonBlank("Enter your choice: ");
    }

    public String cassetteMenuAdd() {
        consoleUi.divideScreen();
        consoleUi.println("Welcome to the Cassette Menu!");
        consoleUi.println("Select denomination of cassette");
        consoleUi.println("1. 5");
        consoleUi.println("2. 10");
        consoleUi.println("3. 20");
        consoleUi.println("4. 50");
        consoleUi.println("0. Cancel");
        return consoleUi.readNonBlank("Enter your choice: ");
    }

    public AccountRegistrationDto accountRegistrationMenu() {
            consoleUi.divideScreen();
            consoleUi.println("Welcome to the Account Registration Menu!");
            String passportId = consoleUi.passportBlank("Enter your passport ID: ");
            String name = consoleUi.nameBlank("Enter your name: ");
            String surname = consoleUi.surnameBlank("Enter your surname: ");
            String pin = consoleUi.pinBlank("Enter your pin: ");
            String currency = "EUR";
            return new AccountRegistrationDto(name, surname, passportId, currency, pin);
    }

    public void finishedRegistrationInfo(String cardNumber) {
        consoleUi.divideScreen();
        consoleUi.showSuccess("Account registration successful!");
        consoleUi.println("Pan of your card is: " + cardNumber);
        consoleUi.waitingInput();
    }

    public String cassetteListMenu(Atm atm) {
        int counter = 1;
        Atm updatedAtm = atmRepository.getAtmById(atm.getId());
        Map<Integer, Cassette> cassettesMap = new HashMap<>();
        List<Cassette> cassettes = updatedAtm.getCassettes();
        consoleUi.divideScreen();
        consoleUi.println("Welcome to the Cassette List Menu!");
        for (Cassette cassette : cassettes) {
            cassettesMap.put(counter, cassette);
            consoleUi.println(counter + ". " + cassette.toString());
            counter++;
        }
        consoleUi.println("0. Exit from update menu.");
        String number = consoleUi.readNonBlank("Select cassette : ");
        if (number.equals("0")) {
            return "0";
        }
        if (cassettesMap.get(Integer.parseInt(number)) == null) {
            throw new IllegalArgumentException("Choice the correct number of cassette");
        }
        return cassettesMap.get(Integer.parseInt(number)).getId().toString();
    }

    public String getCardNumber() {
        return consoleUi.cardBlank("Enter card number: ");
    }

    public String transactionOrBalanceMenu() {
            consoleUi.divideScreen();
            consoleUi.println("Welcome to the Transaction Menu!");
            consoleUi.println("What do you want to do?");
            consoleUi.println("1. Check balance");
            consoleUi.println("2. Make transaction");
            consoleUi.println("3. Deposit");
            consoleUi.println("4. Withdraw");
            consoleUi.println("0. Back");
            return consoleUi.readNonBlank("Enter your choice: ");
    }
}
