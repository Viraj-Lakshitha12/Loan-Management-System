package com.test.loan.service;

import com.test.loan.dto.request.LoanDto;
import com.test.loan.dto.response.LoanResponse;

public interface LoanService {
    String createLoan(LoanDto loanDto);

    LoanResponse getLoan(Long id);

    String changeLoanStatus(Long id, String status);

    String ApproveLoan(Long id);

   String RejectLoan(Long id);
}
