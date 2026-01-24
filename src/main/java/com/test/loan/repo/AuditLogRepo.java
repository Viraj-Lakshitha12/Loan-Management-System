package com.test.loan.repo;

import com.test.loan.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {
}