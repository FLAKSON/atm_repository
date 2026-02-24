package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;
import org.maksymtiutiunnyk.atmproject.ui.AtmUi;
import org.springframework.stereotype.Service;

@Service
public class AtmService {
    private final AtmRepository atmRepository;
    private final CustomerRegistrationService customerRegistrationService;
    public AtmService(AtmRepository atmRepository, CustomerRegistrationService customerRegistrationService) {
        this.atmRepository = atmRepository;
        this.customerRegistrationService = customerRegistrationService;
    }
    @Transactional
    public Atm connectToAtm(Long atmId, String location) {
        if (atmRepository.findById(atmId).isEmpty()) {
            createAtm(location);
        }
        return atmRepository.findById(atmId).get();
    }
    @Transactional
    protected void createAtm(String location) {
        atmRepository.save(Atm.createAtm(location));
    }

    public void atmLiveCycle(Atm atm) {
        AtmUi atmUi = new AtmUi(customerRegistrationService);
        atmUi.mainMenu();
    }

}
