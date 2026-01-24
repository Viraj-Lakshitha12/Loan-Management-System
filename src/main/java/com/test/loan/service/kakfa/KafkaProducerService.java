package com.test.loan.service.kakfa;

import com.test.loan.event.LoanApprovedEvent;
import com.test.loan.event.PaymentReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String LOAN_APPROVED_TOPIC = "loan-approved";
    private static final String PAYMENT_RECEIVED_TOPIC = "payment-received";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * මේක Loan approve වෙලා ඉවර වුනාම call කරනවා
     */
    public void publishLoanApproved(LoanApprovedEvent event) {
        log.info("📤 Publishing LoanApprovedEvent for loan: {}", event.getLoanNumber());

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(LOAN_APPROVED_TOPIC, event.getLoanId().toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Event published successfully to topic: {}", LOAN_APPROVED_TOPIC);
            } else {
                log.error("❌ Failed to publish event: {}", ex.getMessage());
            }
        });
    }

    /**
     * මේක Payment එකක් ආවාම call කරනවා
     */
    public void publishPaymentReceived(PaymentReceivedEvent event) {
        log.info("📤 Publishing PaymentReceivedEvent for loan: {}", event.getLoanNumber());

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(PAYMENT_RECEIVED_TOPIC, event.getLoanId().toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("✅ Event published successfully to topic: {}", PAYMENT_RECEIVED_TOPIC);
            } else {
                log.error("❌ Failed to publish event: {}", ex.getMessage());
            }
        });
    }
}