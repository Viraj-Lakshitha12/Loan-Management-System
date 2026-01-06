package com.test.loan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponse {
    private Long id;
    private Long loanId;
    private Double amount;
    private String paymentDate;
    private String paymentType;
}