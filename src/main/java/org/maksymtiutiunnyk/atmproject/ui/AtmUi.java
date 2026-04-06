package org.maksymtiutiunnyk.atmproject.ui;

import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.service.*;
import org.springframework.stereotype.Component;

@Component
public class AtmUi {

    private final CardService cardService;
    private final AtmService atmService;
    private final CustomerRegistrationService customerRegistrationService;
    private final ConsoleUi console;

    public AtmUi(CardService cardService,  AtmService atmService, CustomerRegistrationService customerRegistrationService, ConsoleUi console) {
        this.cardService = cardService;
        this.atmService = atmService;
        this.customerRegistrationService = customerRegistrationService;
        this.console = console;
    }


    public void mainMenu(Atm atm) {
        console.divideScreen();
        while (true) {
            atmService.statInteraction(atm);
            console.println("Welcome to the Customer Menu!");
            console.println("1. Register (Register new account)");
            console.println("2. Insert Card (For control your balance or transaction)");
            console.println("0. Cancel (Exit)");
            String choice = console.readNonBlank("Enter your choice: ");
            switch (choice) {
                case "1":
                    console.divideScreen();
                    console.println("You have chosen to register!");
                    accountRegistrationMenu(atm);
                    console.waitingInput();
                    break;
                case "2":
                    console.println("You have chosen to insert card!");
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

    private void accountRegistrationMenu(Atm atm) {
        while (true) {
            console.println("Welcome to the Account Registration Menu!");
            String passportId = console.passportBlank("Enter your passportId: ");
            String name = console.nameBlank("Enter your name: ");
            String surname = console.surnameBlank("Enter your surname: ");
            String pin = console.pinBlank("Enter your pin: ");
            String currency = "EUR";
            try {
                customerRegistrationService.registerCustomer(name, surname, passportId, currency, pin, atm);
                console.showSuccess("Account and Customer Registration Successful!");
                return;
            } catch (Exception e) {
                console.showError(e.getMessage());
            }
        }
    }

    private void transactionOrBalanceMenu(Atm atm) {
        while (true) {
            console.divideScreen();
            console.println("Welcome to the Transaction Menu!");
            console.println("1. Check balance");
            console.println("2. Make transaction");
            console.println("0. Back");
            String choice = console.readNonBlank("Enter your choice: ");
            switch (choice) {
                case "1":
                    balanceMenu();
                    console.waitingInput();
                    break;
                case "2":
                    console.println("You have chosen to make transaction! ");
                    console.waitingInput();
                    break;
                case "0":
                    return;
            }
        }
    }

    private void balanceMenu() {
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
                console.showBalance(cardService.getAccessToAccount(cardNumber, pin));
                break;
            } catch (IllegalArgumentException e) {
                console.showError(e.getMessage());
                pin = console.pinBlank("Enter valid pin: ");
            }
        }
    }
}
