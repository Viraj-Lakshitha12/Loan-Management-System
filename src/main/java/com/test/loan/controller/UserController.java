package com.test.loan.controller;

import com.test.loan.dto.common.ApiResponse;
import com.test.loan.dto.request.UserDto;
import com.test.loan.dto.response.LoginResponse;
import com.test.loan.dto.response.UserResponse;
import com.test.loan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "User Created successfully",
                        userService.saveUser(userDto)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "User found successfully",
                        userService.getUserById(id)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "User logged in successfully",
                        userService.loginUser(userDto.getUsername(), userDto.getPassword())
                )
        );
    }
}
