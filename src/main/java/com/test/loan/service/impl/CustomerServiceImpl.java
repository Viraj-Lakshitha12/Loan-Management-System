package com.test.loan.service.impl;

import com.test.loan.advisor.BusinessException;
import com.test.loan.dto.common.ErrorCode;
import com.test.loan.dto.request.CustomerDto;
import com.test.loan.dto.response.CustomerResponse;
import com.test.loan.entity.Customer;
import com.test.loan.repo.CustomerRepo;
import com.test.loan.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private final CustomerRepo customerRepository;
    @Autowired
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepo customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerResponse saveCustomer(CustomerDto customerDto) {
        if (customerRepository.existsCustomerByNic(customerDto.getNic())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }
        Customer save = customerRepository.save(modelMapper.map(customerDto, Customer.class));
        return modelMapper.map(save, CustomerResponse.class);
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
