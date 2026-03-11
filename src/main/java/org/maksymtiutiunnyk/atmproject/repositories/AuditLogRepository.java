package org.maksymtiutiunnyk.atmproject.repositories;

import org.maksymtiutiunnyk.atmproject.entites.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
