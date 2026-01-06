package com.test.loan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String username;
    private String email;
    private String role;
    private String status;
    private String password;
}
