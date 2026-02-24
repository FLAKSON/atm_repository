package org.maksymtiutiunnyk.atmproject.atm;

import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.service.AtmService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AtmLauncher implements CommandLineRunner {
    private final AtmService atmService;
    public AtmLauncher(AtmService atmService) {
        this.atmService = atmService;
    }
    @Override
    public void run(String... args) throws Exception {
        Atm atm = atmService.connectToAtm(1L, "Center");
        atmService.atmLiveCycle(atm);
    }
}

//    private final CustomerRegistrationService customerRegistrationService;
//    private final AtmService atmService;
//    public AtmLauncher(CustomerRegistrationService customerRegistrationService, AtmService atmService) {
//        this.customerRegistrationService = customerRegistrationService;
//        this.atmService = atmService;
//    }
//То что снизу было в строке run
//    ConsoleUi console = new ConsoleUi();
//        atmService.connectToAtm(1L);
//        AtmUi atmUi = new AtmUi(console, customerRegistrationService);
//        atmUi.mainMenu();