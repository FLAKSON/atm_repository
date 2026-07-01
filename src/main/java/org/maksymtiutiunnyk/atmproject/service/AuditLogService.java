package org.maksymtiutiunnyk.atmproject.service;

import jakarta.transaction.Transactional;
import org.maksymtiutiunnyk.atmproject.entities.Atm;
import org.maksymtiutiunnyk.atmproject.entities.AuditLog;
import org.maksymtiutiunnyk.atmproject.entities.Card;
import org.maksymtiutiunnyk.atmproject.enums.AuditLogActorsType;
import org.maksymtiutiunnyk.atmproject.enums.AuditLogStatuses;
import org.maksymtiutiunnyk.atmproject.enums.AuditSeverity;
import org.maksymtiutiunnyk.atmproject.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    private void saveAudit(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }

    public void registrationStarted(Atm atm) {
        saveAudit(
                AuditLog.createAuditLog(
                AuditLogStatuses.REGISTRATION_STARTED,
                AuditLogActorsType.USER,
                AuditSeverity.INFO,
                "Registration user has begun.",
                null,
                atm,
                null,
                null
                )
        );
    }

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

    public void registrationSuccessful(Atm atm, Card card) {
         saveAudit(
                 AuditLog.createAuditLog(
                 AuditLogStatuses.REGISTRATION_SUCCESS,
                 AuditLogActorsType.USER,
                 AuditSeverity.INFO,
                 "Registration successful.",
                 null,
                 atm,
                 card,
                 null
                 )
         );
    }

    public void cassetteInserted(Atm atm) {
        saveAudit(
                AuditLog.createAuditLog(
                        AuditLogStatuses.CASSETTE_STATUS_CHANGED,
                        AuditLogActorsType.ADMIN,
                        AuditSeverity.SECURITY,
                        "Cassette has been inserting.",
                        null,
                        atm,
                        null,
                        null
                )
        );
    }

    public void cassetteUpdated(Atm atm) {
        saveAudit(
                AuditLog.createAuditLog(
                        AuditLogStatuses.CASSETTE_STATUS_CHANGED,
                        AuditLogActorsType.ADMIN,
                        AuditSeverity.SECURITY,
                        "Cassette has been update",
                        null,
                        atm,
                        null,
                        null
                )
        );
    }

    public void cassetteDeleted(Atm atm) {
        saveAudit(
                AuditLog.createAuditLog(
                        AuditLogStatuses.CASSETTE_STATUS_CHANGED,
                        AuditLogActorsType.ADMIN,
                        AuditSeverity.SECURITY,
                        "Cassette has been deleted",
                        null,
                        atm,
                        null,
                        null
                )
        );
    }
}
