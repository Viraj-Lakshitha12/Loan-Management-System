package com.test.loan.service.impl;

import com.test.loan.advisor.BusinessException;
import com.test.loan.config.mapper.PaymentMapper;
import com.test.loan.dto.common.ErrorCode;
import com.test.loan.dto.request.PaymentDto;
import com.test.loan.dto.response.PaymentResponse;
import com.test.loan.entity.Loan;
import com.test.loan.entity.Payment;
import com.test.loan.enums.LoanStatus;
import com.test.loan.event.PaymentReceivedEvent;
import com.test.loan.repo.LoanRepo;
import com.test.loan.repo.PaymentRepo;
import com.test.loan.service.PaymentService;
import com.test.loan.service.kakfa.KafkaProducerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final LoanRepo loanRepo;
    private final PaymentMapper paymentMapper;
    private final KafkaProducerService kafkaProducer;

    public PaymentServiceImpl(
            PaymentRepo paymentRepo,
            LoanRepo loanRepo,
            PaymentMapper paymentMapper,
            KafkaProducerService kafkaProducer) {
        this.paymentRepo = paymentRepo;
        this.loanRepo = loanRepo;
        this.paymentMapper = paymentMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public PaymentResponse makePayment(PaymentDto paymentDto) {

        Loan loan = loanRepo.findById(paymentDto.getLoanId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.LOAN_NOT_FOUND,
                        "Loan not found with id: " + paymentDto.getLoanId()
                ));

        if (loan.getLoanStatus() != LoanStatus.APPROVED) {
            throw new BusinessException(
                    ErrorCode.VALIDATION_FAILED,
                    "Payments allowed only for Approved loans"
            );
        }

        Payment payment = paymentMapper.toEntity(paymentDto);
        payment.setLoan(loan);

        Payment saved = paymentRepo.save(payment);

        //  PUBLISH KAFKA EVENT
        PaymentReceivedEvent event = new PaymentReceivedEvent(
                saved.getId(),
                loan.getId(),
                loan.getLoanNumber(),
                BigDecimal.valueOf(saved.getAmount()),
                saved.getPaymentType().name(),
                LocalDateTime.now()
        );

        kafkaProducer.publishPaymentReceived(event);

        return paymentMapper.toResponse(saved);
    }
}