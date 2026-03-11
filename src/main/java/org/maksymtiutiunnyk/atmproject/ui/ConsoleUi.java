package org.maksymtiutiunnyk.atmproject.ui;

import org.maksymtiutiunnyk.atmproject.entites.Account;
import org.maksymtiutiunnyk.atmproject.entites.Card;
import org.maksymtiutiunnyk.atmproject.entites.Customer;

import java.util.Scanner;

public class ConsoleUi {
    private final Scanner scanner;

    public ConsoleUi() {
        this.scanner = new Scanner(System.in);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public String printReadLine(String prompt) {
        println(prompt);
        return scanner.nextLine();
    }

    public String readNonBlank(String prompt) {
        while (true) {
            String result = printReadLine(prompt).trim();
            if (!result.isEmpty()) {
                return result;
            }
            showError("It's can't be blank!");
        }
    }

    public String nameBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt).toUpperCase().trim();
            try {
                return Customer.validateName(result);
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    public String surnameBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt).toUpperCase().trim();
            try {
                return Customer.validateSurname(result);
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    public String passportBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt).toUpperCase().trim();
            try {
                return Customer.validatePassportFormat(result);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    public String pinBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt);
            try {
                return Card.validatePin(result);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    public String cardBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt);
            try {
                return Card.validateEnteredPan(result);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    public void showBalance(Account account) {
        println("Your balance: " + account.getAvailableBalance() + " " + account.getCurrency());
    }

    public void divideScreen() {
        System.out.println("------------------------------------------------------------------------------");
    }

    public void showError(String error) {
        println("ERROR " + error);
    }

    public void showSuccess(String success) {
        println("SUCCESS " + success);
    }
}
