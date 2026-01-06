package com.test.loan.dto.response;

import com.test.loan.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanResponse {
    private Long id;
    private String loanNumber;
    private Long customerId;
    private Double principalAmount;
    private Double interestRate;
    private LoanStatus loanStatus;
    private LocalDateTime createdAt;
}
