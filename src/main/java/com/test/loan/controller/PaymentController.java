package com.test.loan.controller;

import com.test.loan.dto.common.ApiResponse;
import com.test.loan.dto.request.PaymentDto;
import com.test.loan.dto.response.PaymentResponse;
import com.test.loan.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    @Autowired
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> makePayment(@RequestBody PaymentDto paymentDto) {
        System.out.println("Received payment request: " + paymentDto);
        PaymentResponse paymentResponse = paymentService.makePayment(paymentDto);
        return ResponseEntity.ok(ApiResponse.success(200, "Payment made successfully", paymentResponse));
    }
}
