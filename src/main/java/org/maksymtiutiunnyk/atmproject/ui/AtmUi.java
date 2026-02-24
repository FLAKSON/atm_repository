package org.maksymtiutiunnyk.atmproject.ui;

import org.maksymtiutiunnyk.atmproject.service.CustomerRegistrationService;

public class AtmUi {
    private final CustomerRegistrationService customerRegistrationService;

    public AtmUi(CustomerRegistrationService customerRegistrationService) {
        this.customerRegistrationService = customerRegistrationService;
    }

    private final ConsoleUi console = new ConsoleUi();

    public void accountRegistrationMenu() {
        console.println("Welcome to the Account Registration Menu!");
        String passportId = console.passportBlank("Enter your passportId: ");
        String name = console.nameBlank("Enter your name: ");
        String surname = console.surnameBlank("Enter your surname: ");
        String pin = console.pinBlank("Enter your pin: ");
        String currency = "EUR";
        try {
            customerRegistrationService.registerCustomer(name, surname, passportId, currency, pin);
            console.showSuccess("Account and Customer Registration Successful!");
        } catch (Exception e) {
            console.showError(e.getMessage());
        }
    }

    public void mainMenu() {
        while (true) {
            console.println("Welcome to the Customer Menu!");
            console.println("1. Register (Register new account)");
            console.println("2. Insert Card (For control your balance or transaction)");
            console.println("0. Cancel (Exit)");
            String choice = console.readNonBlank("Enter your choice: ");
            switch (choice) {
                case "1":
                    console.println("You have chosen to register! ");
                    accountRegistrationMenu();
                    break;
                case "2":
                    console.println("Enter Card Name: ");
                    break;
                case "0":
                    console.println("Goodbye!");
                    return;
            }
            console.println("---------------------------------------------------------------------------------------------------------------------------");
        }
    }

}
