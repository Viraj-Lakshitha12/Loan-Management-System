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
import org.springframework.stereotype.Service;
import com.test.loan.entity.Customer;
import com.test.loan.service.kakfa.KafkaProducerService;
import com.test.loan.event.LoanApprovedEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepo loanRepo;
    private final ModelMapper modelMapper;
    private final CustomerRepo customerRepo;
    private final KafkaProducerService kafkaProducer;

    public LoanServiceImpl(LoanRepo loanRepo,
                           ModelMapper modelMapper,
                           CustomerRepo customerRepo,
                           KafkaProducerService kafkaProducer) {
        this.loanRepo = loanRepo;
        this.modelMapper = modelMapper;
        this.customerRepo = customerRepo;
        this.kafkaProducer = kafkaProducer;
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
        Loan savedLoan = loanRepo.save(loan);

        // 🔥 KAFKA EVENT PUBLISH කරනවා (මෙතන තමයි magic එක!)
        Customer customer = customerRepo.findById(loan.getCustomerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));

        LoanApprovedEvent event = new LoanApprovedEvent(
                savedLoan.getId(),
                savedLoan.getLoanNumber(),
                customer.getId(),
                customer.getFullName(),
                BigDecimal.valueOf(savedLoan.getPrincipalAmount()),
                savedLoan.getLoanStatus().name(),
                LocalDateTime.now(),
                "SYSTEM"  // මේක logged user එක්කෙන් ගන්න පුළුවන්
        );

        kafkaProducer.publishLoanApproved(event);

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