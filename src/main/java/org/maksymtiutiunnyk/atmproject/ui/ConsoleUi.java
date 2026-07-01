package org.maksymtiutiunnyk.atmproject.ui;

import org.maksymtiutiunnyk.atmproject.entities.Account;
import org.maksymtiutiunnyk.atmproject.validation.InputValidator;
import org.springframework.stereotype.Component;

import java.util.Scanner;
@Component
public class ConsoleUi {
    private final static Scanner scanner = new Scanner(System.in);

    // Просто упрощение принта.
    public void println(String message) {
        System.out.println(message);
    }

    // Ожидание ввода
    public String printReadLine(String prompt) {
        println(prompt);
        return scanner.nextLine();
    }

    // Проверка что ввод не пуст.
    public String readNonBlank(String prompt) {
        while (true) {
            String result = printReadLine(prompt);
            if (!result.isEmpty()) {
                return result;
            }
            showError("It's can't be blank!");
        }
    }

    // Валидация имени.
    public String nameBlank(String name) {
        while (true) {
            String result = readNonBlank(name);
            try {
                return InputValidator.validateName(result);
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    // Валидация фамилии.
    public String surnameBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt);
            try {
                return InputValidator.validateSurname(result);
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        }
    }

    // Валидация паспорта
    public String passportBlank(String passport) {
        while (true) {
            String result = readNonBlank(passport);
            try {
                return InputValidator.validatePassportId(result);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    public String pinBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt);
            try {
                return InputValidator.validatePin(result);
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    public String cardBlank(String prompt) {
        while (true) {
            String result = readNonBlank(prompt);
            try {
                return InputValidator.validatePan(result);
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

    public void waitingInput() {
        println("Press enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public void showError(String error) {
        println("ERROR " + error);
    }

    public void showSuccess(String success) {
        println("SUCCESS " + success);
    }
}
