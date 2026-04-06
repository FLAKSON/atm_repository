package org.maksymtiutiunnyk.atmproject.manager;

import org.jspecify.annotations.NonNull;
import org.maksymtiutiunnyk.atmproject.service.*;
import org.maksymtiutiunnyk.atmproject.ui.AtmUi;
import org.maksymtiutiunnyk.atmproject.ui.ConsoleUi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AtmManager implements CommandLineRunner {
    private final CardService cardService;
    private final AtmService atmService;
    private final CustomerRegistrationService customerRegistrationService;
    private final ConsoleUi console;

    public AtmManager(CardService cardService, AtmService atmService, CustomerRegistrationService customerRegistrationService, ConsoleUi console) {
        this.cardService = cardService;
        this.atmService = atmService;
        this.customerRegistrationService = customerRegistrationService;
        this.console = console;
    }

    @Override
    public void run(String @NonNull ... args) {
        AtmUi atmUi = new AtmUi(cardService, atmService, customerRegistrationService, console);
        atmUi.mainMenu(atmService.connectToAtm(1L, "Center"));
    }
}