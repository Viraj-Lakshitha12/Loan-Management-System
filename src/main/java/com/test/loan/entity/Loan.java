package com.test.loan.entity;

import com.test.loan.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "LOANS")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String loanNumber;
    @Column(nullable = false)
    private Long customerId;
    @Column(nullable = false)
    private Double principalAmount;
    @Column(nullable = false)
    private Double interestRate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus = LoanStatus.PENDING;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}
