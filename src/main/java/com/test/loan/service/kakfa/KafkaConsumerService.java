package com.test.loan.service.kakfa;

import com.test.loan.entity.AuditLog;
import com.test.loan.event.LoanApprovedEvent;
import com.test.loan.event.PaymentReceivedEvent;
import com.test.loan.repo.AuditLogRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final AuditLogRepo auditLogRepo;

    public KafkaConsumerService(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    /**
     * after the  Loan approval event automatically call this method
     */
    @KafkaListener(
            topics = "loan-approved",
            groupId = "loan-management-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeLoanApproved(LoanApprovedEvent event) {
        log.info("📥 Received LoanApprovedEvent: Loan {} approved", event.getLoanNumber());

        try {
            // Audit log save
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("LOAN_APPROVED");
            auditLog.setEntityType("LOAN");
            auditLog.setEntityId(event.getLoanId());
            auditLog.setDetails("Loan " + event.getLoanNumber() +
                    " approved for customer " + event.getCustomerName() +
                    " - Amount: " + event.getAmount());
            auditLog.setPerformedBy(event.getApprovedBy());
            auditLog.setPerformedAt(LocalDateTime.now());

            auditLogRepo.save(auditLog);

            log.info("✅ Audit log saved for loan approval");

            // - Send Email
            // - SMS notification
            // - Accounting system integration
            // - Update customer dashboard

        } catch (Exception e) {
            log.error("❌ Error processing loan approved event: {}", e.getMessage());
            // Handle exception appropriately
        }
    }

    /**
     * after the  Payment received event automatically call this method
     */
    @KafkaListener(
            topics = "payment-received",
            groupId = "loan-management-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentReceived(PaymentReceivedEvent event) {
        log.info("📥 Received PaymentReceivedEvent: Payment {} for loan {}",
                event.getAmount(), event.getLoanNumber());

        try {
            // Audit log එකට save කරනවා
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("PAYMENT_RECEIVED");
            auditLog.setEntityType("PAYMENT");
            auditLog.setEntityId(event.getPaymentId());
            auditLog.setDetails("Payment of Rs." + event.getAmount() +
                    " received for loan " + event.getLoanNumber() +
                    " via " + event.getPaymentMethod());
            auditLog.setPerformedBy("SYSTEM");
            auditLog.setPerformedAt(LocalDateTime.now());

            auditLogRepo.save(auditLog);

            log.info("✅ Audit log saved for payment");

            // - Payment confirmation email
            // - Update loan balance
            // - Generate receipt

        } catch (Exception e) {
            log.error("❌ Error processing payment received event: {}", e.getMessage());
        }
    }
}