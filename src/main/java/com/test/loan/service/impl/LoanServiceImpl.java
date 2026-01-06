package com.test.loan.service.impl;

import com.test.loan.advisor.BusinessException;
import com.test.loan.dto.common.ErrorCode;
import com.test.loan.dto.request.LoanDto;
import com.test.loan.dto.response.LoanResponse;
import com.test.loan.entity.Loan;
import com.test.loan.enums.LoanStatus;
import com.test.loan.repo.CustomerRepo;
import com.test.loan.repo.LoanRepo;
import com.test.loan.service.LoanService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private final LoanRepo loanRepo;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final CustomerRepo customerRepo;

    public LoanServiceImpl(LoanRepo loanRepo, ModelMapper modelMapper, CustomerRepo customerRepo) {
        this.loanRepo = loanRepo;
        this.modelMapper = modelMapper;
        this.customerRepo = customerRepo;
    }

    @Override
    public String createLoan(LoanDto loanDto) {

        customerRepo.findById(loanDto.getCustomerId())
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.CUSTOMER_NOT_FOUND,
                                "Customer not found with id: " + loanDto.getCustomerId()
                        )
                );

        if (loanRepo.existsByLoanNumber(loanDto.getLoanNumber())) {
            throw new BusinessException(
                    ErrorCode.LOAN_ALREADY_EXISTS,
                    "Loan already exists with loan number: " + loanDto.getLoanNumber()
            );
        }
        Loan loan = modelMapper.map(loanDto, Loan.class);
        System.out.println("BEFORE FLUSH: " + loan);
        Loan savedLoan = loanRepo.save(loan);

        return "Loan Created with ID: " + savedLoan.getId();
    }


    @Override
    public LoanResponse getLoan(Long id) {

        Loan loan = loanRepo.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.LOAN_NOT_FOUND,
                                "Loan not found with id: " + id
                        )
                );

        return modelMapper.map(loan, LoanResponse.class);
    }

    @Override
    public String changeLoanStatus(Long id, String status) {
        Loan loan = loanRepo.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.LOAN_NOT_FOUND,
                                "Loan not found with id: " + id
                        )
                );
        if (loan.getLoanStatus() == LoanStatus.CLOSED) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED,
                    "Cannot change status of a closed loan");
        }

        loan.setLoanStatus(LoanStatus.valueOf(status));
        loanRepo.save(loan);

        return "Loan status updated to: " + status;
    }

    @Override
    public String ApproveLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.LOAN_NOT_FOUND,
                        "Loan not found with id: " + loanId));

        if (loan.getLoanStatus() == LoanStatus.APPROVED) {
            throw new BusinessException(
                    ErrorCode.VALIDATION_FAILED,
                    "Loan already approved"
            );
        }

        if (loan.getLoanStatus() == LoanStatus.REJECTED) {
            throw new BusinessException(
                    ErrorCode.VALIDATION_FAILED,
                    "Rejected loan cannot be approved"
            );
        }

        loan.setLoanStatus(LoanStatus.APPROVED);
        loanRepo.save(loan);

        return "Loan approved successfully";
    }

    @Override
    public String RejectLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.LOAN_NOT_FOUND,
                        "Loan not found with id: " + loanId));

        if (loan.getLoanStatus() == LoanStatus.APPROVED) {
            throw new BusinessException(
                    ErrorCode.VALIDATION_FAILED,
                    "Approved loan cannot be rejected"
            );
        }

        loan.setLoanStatus(LoanStatus.REJECTED);
        loanRepo.save(loan);

        return "Loan rejected successfully";
    }

}
