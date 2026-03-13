package org.maksymtiutiunnyk.atmproject.atm;

import org.jspecify.annotations.NonNull;
import org.maksymtiutiunnyk.atmproject.service.AtmService;
import org.maksymtiutiunnyk.atmproject.service.CardService;
import org.maksymtiutiunnyk.atmproject.service.CustomerRegistrationService;
import org.maksymtiutiunnyk.atmproject.ui.AtmUi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class AtmLauncher implements CommandLineRunner {
    private final CardService cardService;
    private final AtmService atmService;
    private final CustomerRegistrationService customerRegistrationService;

    public AtmLauncher(CardService cardService, AtmService atmService, CustomerRegistrationService customerRegistrationService) {
        this.cardService = cardService;
        this.atmService = atmService;
        this.customerRegistrationService = customerRegistrationService;
    }

    @Override
    public void run(String @NonNull ... args) {
        AtmUi atmUi = new AtmUi(cardService, atmService, customerRegistrationService);
        atmUi.mainMenu(atmService.connectToAtm(1L, "Center"));

    }
}