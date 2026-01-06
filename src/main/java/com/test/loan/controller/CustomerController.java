package com.test.loan.controller;

import com.test.loan.dto.common.ApiResponse;
import com.test.loan.dto.request.CustomerDto;
import com.test.loan.dto.response.CustomerResponse;
import com.test.loan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(ApiResponse.success(200, "Customer created successfully",
                customerService.saveCustomer(customerDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Customer found successfully",
                customerService.getCustomer(id)));
    }
}
