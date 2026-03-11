package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entites.Atm;
import org.maksymtiutiunnyk.atmproject.entites.AuditLog;
import org.maksymtiutiunnyk.atmproject.enums.AuditLogActorsType;
import org.maksymtiutiunnyk.atmproject.enums.AuditLogStatuses;
import org.maksymtiutiunnyk.atmproject.enums.AuditSeverity;
import org.maksymtiutiunnyk.atmproject.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void registrationStarted(Atm atm) {
        saveAudit(
                AuditLog.createAuditLog(
                AuditLogStatuses.REGISTRATION_STARTED,
                AuditLogActorsType.USER,
                AuditSeverity.INFO,
                "Registration user has begun",
                null,
                atm,
                null,
                null
                )
        );
    }
    @Transactional
    public void registrationFailed(Atm atm, Exception e) {
        saveAudit(
                AuditLog.createAuditLog(
                AuditLogStatuses.REGISTRATION_FAILED,
                AuditLogActorsType.USER,
                AuditSeverity.INFO,
                "Registration has failed: " + e.getMessage(),
                null,
                atm,
                null,
                null
                )
        );
    }
    @Transactional
    public void registrationSuccessful(Atm atm) {
         saveAudit(
                 AuditLog.createAuditLog(
                 AuditLogStatuses.REGISTRATION_SUCCESS,
                 AuditLogActorsType.USER,
                 AuditSeverity.INFO,
                 "Registration successful",
                 null,
                 atm,
                 null,
                 null
                 )
         );
    }
    protected void saveAudit(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}
