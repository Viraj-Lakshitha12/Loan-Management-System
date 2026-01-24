package com.test.loan.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApprovedEvent {
    private Long loanId;
    private String loanNumber;
    private Long customerId;
    private String customerName;
    private BigDecimal amount;
    private String status;
    private LocalDateTime approvedAt;
    private String approvedBy;
}