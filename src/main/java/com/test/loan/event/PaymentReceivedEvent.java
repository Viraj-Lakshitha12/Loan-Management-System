package com.test.loan.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceivedEvent {
    private Long paymentId;
    private Long loanId;
    private String loanNumber;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paidAt;
}