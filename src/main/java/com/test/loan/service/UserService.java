package com.test.loan.service;

import com.test.loan.dto.request.UserDto;
import com.test.loan.dto.response.LoginResponse;
import com.test.loan.dto.response.UserResponse;

public interface UserService {
    UserResponse saveUser(UserDto userDto);

    UserResponse getUserById(Long id);

    LoginResponse loginUser(String username, String password);
}
