package com.test.loan.config.mapper;

import com.test.loan.dto.request.PaymentDto;
import com.test.loan.dto.response.PaymentResponse;
import com.test.loan.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loan", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    Payment toEntity(PaymentDto dto);

    @Mapping(target = "loanId", source = "loan.id")
    @Mapping(target = "paymentDate", expression = "java(payment.getPaymentDate().toString())")
    @Mapping(target = "paymentType", expression = "java(payment.getPaymentType().name())")
    PaymentResponse toResponse(Payment payment);
}
