package com.test.loan.dto.request;

import com.test.loan.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {
    private Long loanId;
    private Double amount;
    private PaymentType paymentType;

}