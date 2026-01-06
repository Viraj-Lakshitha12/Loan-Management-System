package com.test.loan.service;

import com.test.loan.dto.request.UserDto;
import com.test.loan.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserResponse saveUser(UserDto userDto);

    UserResponse getUserById(Long id);

    UserDetails loadUserByUsername(String username);


}
