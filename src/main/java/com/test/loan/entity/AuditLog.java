package com.test.loan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LOAN_APPROVED, PAYMENT_RECEIVED
    @Column(nullable = false)
    private String action;

    // LOAN, PAYMENT, CUSTOMER
    @Column(nullable = false)
    private String entityType;

    // loanId / paymentId
    @Column(nullable = false)
    private Long entityId;

    @Column(length = 1000)
    private String details;

    // SYSTEM / USERNAME
    private String performedBy;

    private LocalDateTime performedAt;
}
