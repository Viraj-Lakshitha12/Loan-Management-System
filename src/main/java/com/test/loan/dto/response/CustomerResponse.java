package com.test.loan.dto.response;

import com.test.loan.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResponse {
    private Long id;
    private String fullName;
    private String nic;
    private String address;
    private String phone;
    private CustomerStatus status;}
