package org.maksymtiutiunnyk.atmproject.service;

import org.maksymtiutiunnyk.atmproject.dto.AccountRegistrationDto;
import org.maksymtiutiunnyk.atmproject.entities.Account;
import org.maksymtiutiunnyk.atmproject.entities.Atm;
import org.maksymtiutiunnyk.atmproject.enums.DenominationTypes;
import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;
import org.maksymtiutiunnyk.atmproject.ui.AtmUi;
import org.maksymtiutiunnyk.atmproject.ui.ConsoleUi;
import org.maksymtiutiunnyk.atmproject.validation.InputValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AtmManager {
    private final AtmUi atmUi;
    private final ConsoleUi consoleUi;
    private final AtmService atmService;
    private final CardService cardService;
    private final SessionService sessionService;
    private final CassetteService cassetteService;
    private final CustomerRegistrationService customerRegistrationService;
    private final WithdrawService withdrawService;
    private final AtmRepository atmRepository;

    public AtmManager(AtmService atmService, AtmUi atmUi, CardService cardService, ConsoleUi consoleUi, SessionService sessionService, CassetteService cassetteService, CustomerRegistrationService customerRegistrationService, WithdrawService withdrawService, AtmRepository atmRepository) {
        this.atmService = atmService;
        this.atmUi = atmUi;
        this.cardService = cardService;
        this.consoleUi = consoleUi;
        this.sessionService = sessionService;
        this.cassetteService = cassetteService;
        this.customerRegistrationService = customerRegistrationService;
        this.withdrawService = withdrawService;
        this.atmRepository = atmRepository;
    }

    private Atm connectToAtm() {
        return atmService.connectToAtm(1L, "Center");
    }

    public void atmManager() {
        final Atm atm = connectToAtm();
        atmService.statInteraction(atm);
        while (true) {
            String choice = atmUi.mainMenu();
            switch (choice) {
                case "1":
                    atmRegistrationManager(atm);
                    break;
                case "2":
                    String cardNumber = checkPan();
                    sessionService.startSession(cardNumber, atm);
                    try {
                        atmOperations(atm, cardNumber, "1");
                    } catch (Exception e) {
                        consoleUi.showError(e.getMessage());
                    }
                    break;
                case "0":
                    consoleUi.println("Goodbye!");
                    atmService.closeInteraction(atm);
                    return;
                case "0523":
                    consoleUi.println("Admin mode is on.");
                    atmService.adminInteraction(atm);
                    adminModeManager("1");
                    break;
                default:
                    consoleUi.println("Invalid choice, please try again.");
            }
        }
    }

    private void adminModeManager(String atm) {
        while (true) {
            String choice = atmUi.adminMenu();
            switch (choice) {
                case "1":
                    addCassetteToAtm(atm);
                    break;
                case "2":
                    updateCassetteInAtm(atm);
                    break;
                case "3":
                    pullOutCassetteInAtm(atm);
                    break;
                case "4":
                    showCassettes(atm);
                    break;
                case "0":
                    Atm gettedAtm = atmRepository.getAtmById(Long.parseLong(atm));
                    atmService.closeInteraction(gettedAtm);
                    return;
                default:
                    consoleUi.println("Invalid choice, please try again.");
            }
        }
    }

    private void showCassettes(String atm) {
        while (true) {
            Atm gettedAtm = atmRepository.getAtmById(Long.parseLong(atm));
            String choice = atmUi.cassetteListMenu(gettedAtm);
            if (choice.equals("0")) {
                return;
            }
        }
    }

    private void pullOutCassetteInAtm(String atm) {
        consoleUi.println("Select cassette to pull out.");
        Atm gettedAtm = atmRepository.getAtmById(Long.parseLong(atm));
        while (true) {
            try {
                String choice = atmUi.cassetteListMenu(atmService.connectToAtm(gettedAtm.getId(), gettedAtm.getLocation()));
                if (choice.equals("0")) {
                    return;
                }
                try {
                    cassetteService.deleteCassetteInAtm(choice, gettedAtm);
                } catch (Exception e) {
                    consoleUi.showError(e.getMessage());
                }
            } catch (Exception e) {
                consoleUi.showError(e.getMessage());
            }
        }
    }

    private void updateCassetteInAtm(String atm) {
        Atm gettedAtm = atmRepository.getAtmById(Long.parseLong(atm));
        while (true) {
            consoleUi.println("Select cassette to update.");
            try {
                String choice = atmUi.cassetteListMenu(atmService.connectToAtm(gettedAtm.getId(), gettedAtm.getLocation()));
                if (choice.equals("0")) {
                    return;
                }
                try {
                    cassetteService.updateCassetteInAtm(choice, gettedAtm);
                } catch (Exception e) {
                    consoleUi.showError(e.getMessage());
                }
            } catch (Exception e) {
                consoleUi.showError(e.getMessage());
            }
        }
    }

    private void addCassetteToAtm(String atm) {
        Atm gettedAtm = atmRepository.getAtmById(Long.parseLong(atm));
        while (true) {
            try {
                String choice = atmUi.cassetteMenuAdd();
                if (choice.equals("0")) {
                    return;
                }
                try {
                    DenominationTypes denomination = InputValidator.validateDenomination(choice);
                    if (denomination != null) {
                        cassetteService.createCassetteAndAddToAtm(denomination, 500, gettedAtm);
                    }
                } catch (Exception e) {
                    consoleUi.showError(e.getMessage());
                }
            } catch (Exception e) {
                consoleUi.showError(e.getMessage());
            }
        }
    }

    private void atmRegistrationManager(Atm atm) {
        AccountRegistrationDto accountRegistrationDto =  atmUi.accountRegistrationMenu();
        customerRegistrationService.registerCustomer(
                accountRegistrationDto.name(),
                accountRegistrationDto.surname(),
                accountRegistrationDto.passportId(),
                accountRegistrationDto.currency(),
                accountRegistrationDto.pin(),
                atm
        );
        atmUi.finishedRegistrationInfo(cardService.getPanToViewUser(accountRegistrationDto.passportId()));
    }

    private void atmOperations(Atm atm, String cardNumber, String atmId) {
        while (true) {
            String choice = atmUi.transactionOrBalanceMenu();
            switch (choice) {
                case "1":
                    checkBalanceManager(cardNumber);
                    if (!sessionService.isSessionAuthorized()) {
                        return;
                    }
                    break;
                case "2":
                    String targetCardNumber = "123"; // Заглушка
                    makeTransactionManager(atm, cardNumber, targetCardNumber);
                    break;
                case "3":
                    System.out.println("In development.");
                    break;
                case "4":
                    withdrawManager(cardNumber, atmId);
                    if (!sessionService.isSessionAuthorized()) {
                        return;
                    }
                case "0":
                    sessionService.closeSession();
                    return;
                default:
                    consoleUi.println("Invalid choice, please try again.");
            }
        }
    }
    
    private void withdrawManager(String cardNumber, String atmId) {
        while (true) {
            if (sessionService.isSessionAuthorized()) {
                checkAvailable(cardNumber);
                String amount = atmUi.withdrawMenu();
                withdrawService.makeWithdraw(atmId,amount,cardNumber);
                if (amount.equals("0")) {
                    return;
                }
            }
            else {
                checkAvailable(cardNumber);
                sessionService.authorizedSession();
                String amount = atmUi.withdrawMenu();
                withdrawService.makeWithdraw(atmId,amount,cardNumber);
                if (amount.equals("0")) {
                    return;
                }
            }
        }
    }

    private void checkBalanceManager(String cardNumber) {
        if (sessionService.isSessionAuthorized()) {
            Account account = checkAvailable(cardNumber);
            consoleUi.showBalance(account);
            consoleUi.waitingInput();
        }
        else {
            Account account = checkAvailable(cardNumber);
            sessionService.authorizedSession();
            consoleUi.showBalance(account);
            consoleUi.waitingInput();
        }
    }

    private void makeDepositTransactionManager(Atm atm, String cardNumber) {
        // Заглушка.
    }

    private void makeTransactionManager(Atm atm, String cardNumber, String targetCardNumber) {
        // Недоделано хайпует(почучуть).
        // Черемша.
    }

    private String checkPan() {
        while (true) {
            String cardNumber = atmUi.getCardNumber();
            try {
                cardService.checkPan(cardNumber);
                return cardNumber;
            } catch (Exception e) {
                consoleUi.showError(e.getMessage());
            }
        }
    }

    private Account checkAvailable(String cardNumber) {
        if (sessionService.isSessionAuthorized() && sessionService.isCardAvailable(cardNumber)) {
            return cardService.getAccessToAccount(cardNumber, sessionService.isSessionAuthorized());
        }
        else {
            while (true) {
                String pin = consoleUi.pinBlank("Enter your pin: ");
                try {
                    return cardService.getAccessToAccount(cardNumber, pin);
                } catch (Exception e) {
                    consoleUi.showError(e.getMessage());
                    sessionService.addFailed();
                }
                if (sessionService.isSessionEnded()) {
                    consoleUi.showError("Too many incorrect PIN attempts. Returning to main menu.");
                    return cardService.getAccessToAccount(cardNumber, sessionService.isSessionAuthorized());
                }
            }
        }
    }
}
