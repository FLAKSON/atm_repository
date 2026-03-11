package org.maksymtiutiunnyk.atmproject.ui;

import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.enums.AtmStatuses;
import org.maksymtiutiunnyk.atmproject.service.AtmService;
import org.maksymtiutiunnyk.atmproject.service.CardService;

public class AtmUi {
    private final CardService cardService;
    private final AtmService atmService;

    public AtmUi(CardService cardService,  AtmService atmService) {
        this.cardService = cardService;
        this.atmService = atmService;
    }

    private final ConsoleUi console = new ConsoleUi();

    private void accountRegistrationMenu(Atm atm) {
        console.println("Welcome to the Account Registration Menu!");
        String passportId = console.passportBlank("Enter your passportId: ");
        String name = console.nameBlank("Enter your name: ");
        String surname = console.surnameBlank("Enter your surname: ");
        String pin = console.pinBlank("Enter your pin: ");
        String currency = "EUR";
        try {
            atmService.registerCustomer(name, surname, passportId, currency, pin, atm);
            console.showSuccess("Account and Customer Registration Successful!");
        } catch (Exception e) {
            console.showError(e.getMessage());
        }
    }

    public void mainMenu(Atm atm) {
        while (true) {
            atmService.changeAtmStatus(atm, AtmStatuses.IN_SERVICE);
            console.println("Welcome to the Customer Menu!");
            console.println("1. Register (Register new account)");
            console.println("2. Insert Card (For control your balance or transaction)");
            console.println("0. Cancel (Exit)");
            String choice = console.readNonBlank("Enter your choice: ");
            switch (choice) {
                case "1":
                    console.println("You have chosen to register! ");
                    accountRegistrationMenu(atm);
                    break;
                case "2":
                    console.println("You have chosen to insert card! ");
                    transactionOrBalanceMenu(atm);
                    break;
                case "0":
                    console.println("Goodbye!");
                    atmService.closeInteraction(atm);
                    return;
            }
            console.divideScreen();
        }
    }

    private void transactionOrBalanceMenu(Atm atm) {
        console.println("1. Check balance");
        console.println("2. Make transaction");
        String choice = console.readNonBlank("Enter your choice: ");
        switch (choice) {
            case "1":
                balanceMenu(atm);
                break;
            case "2":
                console.println("You have chosen to make transaction! ");

        }
    }

    private void balanceMenu(Atm atm) {
        console.println("You have chosen to check balance! ");
        String cardNumber = console.cardBlank("Enter card number: ");
        while (true) {
            try {
                cardService.checkPan(cardNumber);
                break;
            } catch (Exception e) {
                console.showError(e.getMessage());
                cardNumber = console.cardBlank("Enter a valid card number: ");
            }
        }
        String pin = console.pinBlank("Enter your pin: ");
        while (true) {
            try {
                console.showBalance(atmService.checkBalance(cardNumber, pin));
                break;
            } catch (Exception e) {
                console.showError(e.getMessage());
                pin = console.pinBlank("Enter valid pin: ");
            }
        }
    }

    private void transactionMenu(Atm atm) {

    }



}
