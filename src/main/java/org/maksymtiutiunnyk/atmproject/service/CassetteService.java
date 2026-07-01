package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entities.Atm;
import org.maksymtiutiunnyk.atmproject.entities.Cassette;
import org.maksymtiutiunnyk.atmproject.enums.DenominationTypes;

import org.maksymtiutiunnyk.atmproject.repositories.AtmRepository;
import org.maksymtiutiunnyk.atmproject.repositories.CassetteRepository;
import org.springframework.stereotype.Service;

@Service
public class CassetteService {


    private final AtmRepository atmRepository;
    private final CassetteRepository cassetteRepository;
    private final AuditLogService auditLogService;

    public CassetteService(AtmRepository atmRepository, CassetteRepository cassetteRepository, AuditLogService auditLogService) {
        this.atmRepository = atmRepository;
        this.cassetteRepository = cassetteRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public void createCassetteAndAddToAtm(DenominationTypes denomination, int count, Atm atm) {
        Atm forSession = atmRepository.findById(atm.getId())
                .orElseThrow(() -> new RuntimeException("Atm not found"));
        Cassette cassette = Cassette.createCassette(denomination, count);
        forSession.addCassette(cassette);
        auditLogService.cassetteInserted(atm);
    }

    @Transactional
    public void updateCassetteInAtm(String cassetteNumber, Atm atm) {
        Long choice = Long.parseLong(cassetteNumber);
        Cassette cassette = cassetteRepository.getCassetteById(choice).orElseThrow(() -> new RuntimeException("Cassette not found"));
        cassette.updateCassette();
        auditLogService.cassetteUpdated(atm);
        cassetteRepository.save(cassette);
    }

    @Transactional
    public void deleteCassetteInAtm(String cassetteNumber, Atm atm) {
        Long choice  = Long.parseLong(cassetteNumber);
        cassetteRepository.deleteById(choice);
        cassetteRepository.flush();
        auditLogService.cassetteDeleted(atm);
    }


}
