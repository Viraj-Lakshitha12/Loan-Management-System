package com.test.loan.service;

import com.test.loan.dto.request.CustomerDto;
import com.test.loan.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse saveCustomer(CustomerDto customerDto);

    CustomerResponse getCustomer(Long id);
}
