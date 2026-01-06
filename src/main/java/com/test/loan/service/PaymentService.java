package com.test.loan.service;

import com.test.loan.dto.request.PaymentDto;
import com.test.loan.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse makePayment(PaymentDto paymentDto);
}
