package org.maksymtiutiunnyk.atmproject.atm;

import org.jspecify.annotations.NonNull;
import org.maksymtiutiunnyk.atmproject.service.AtmService;
import org.maksymtiutiunnyk.atmproject.service.CardService;
import org.maksymtiutiunnyk.atmproject.ui.AtmUi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AtmLauncher implements CommandLineRunner {
    private final CardService cardService;
    private final AtmService atmService;

    public AtmLauncher(CardService cardService, AtmService atmService) {
        this.cardService = cardService;
        this.atmService = atmService;
    }

    @Override
    public void run(String @NonNull ... args) {
        AtmUi atmUi = new AtmUi(cardService, atmService);
        atmUi.mainMenu(atmService.connectToAtm(1L, "Center"));

    }
}