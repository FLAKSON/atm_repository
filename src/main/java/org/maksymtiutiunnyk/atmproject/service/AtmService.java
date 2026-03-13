package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.enums.AtmStatuses;
import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;

import org.springframework.stereotype.Service;


@Service
public class AtmService {
    private final AtmRepository atmRepository;

    public AtmService(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    /*A method that connects the user to specific ATM.*/
    @Transactional
    public Atm connectToAtm(Long atmId, String location) {
        return atmRepository.findById(atmId).orElseGet(() -> createAndGetAtm(location));
    }

    private Atm createAndGetAtm(String location) {
        return atmRepository.save(Atm.createAtm(location));
    }

    /*A methods that change statuses ATM*/
    private void changeAtmStatus(Atm atm, AtmStatuses atmStatus) {
        atm.setAtmStatus(atmStatus);
        atmRepository.save(atm);
    }

    @Transactional
    public void closeInteraction(Atm atm) {
        changeAtmStatus(atm, AtmStatuses.AWAITING);
    }

    @Transactional
    public void statInteraction(Atm atm) {
        changeAtmStatus(atm, AtmStatuses.IN_SERVICE);
    }
}
