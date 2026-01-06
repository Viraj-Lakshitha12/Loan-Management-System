package com.test.loan.dto.request;

import com.test.loan.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDto {
    private String fullName;
    private String nic;
    private String phone;
    private String address;
    private CustomerStatus status;
}
