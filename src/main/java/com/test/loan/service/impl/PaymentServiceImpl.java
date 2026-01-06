package com.test.loan.service.impl;

import com.test.loan.config.mapper.PaymentMapper;
import com.test.loan.dto.request.PaymentDto;
import com.test.loan.dto.response.PaymentResponse;
import com.test.loan.entity.Loan;
import com.test.loan.entity.Payment;
import com.test.loan.enums.LoanStatus;
import com.test.loan.repo.LoanRepo;
import com.test.loan.repo.PaymentRepo;
import com.test.loan.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final LoanRepo loanRepo;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(
            PaymentRepo paymentRepo,
            LoanRepo loanRepo,
            PaymentMapper paymentMapper) {
        this.paymentRepo = paymentRepo;
        this.loanRepo = loanRepo;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponse makePayment(PaymentDto paymentDto) {

        Loan loan = loanRepo.findById(paymentDto.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getLoanStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("Payments allowed only for Approved loans");
        }

        Payment payment = paymentMapper.toEntity(paymentDto);

        // IMPORTANT: set relationship here
        payment.setLoan(loan);

        Payment saved = paymentRepo.save(payment);

        return paymentMapper.toResponse(saved);
    }
}


