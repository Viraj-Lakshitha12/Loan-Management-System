package com.test.loan.dto.request;
import com.test.loan.enums.LoanStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoanDto {
    private String loanNumber;
    private Long customerId;
    private Double principalAmount;
    private Double interestRate;
    private LoanStatus loanStatus;
}
