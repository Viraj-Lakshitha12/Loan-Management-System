package com.test.loan.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String nic;
    private String phone;
    private String address;
    private String status;
}
