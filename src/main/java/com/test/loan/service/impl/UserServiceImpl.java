package com.test.loan.service.impl;

import com.test.loan.advisor.BusinessException;
import com.test.loan.config.mapper.UserMapper;
import com.test.loan.config.security.JwtUtil;
import com.test.loan.dto.common.ErrorCode;
import com.test.loan.dto.request.UserDto;
import com.test.loan.dto.response.LoginResponse;
import com.test.loan.dto.response.UserResponse;
import com.test.loan.entity.User;
import com.test.loan.repo.UserRepo;
import com.test.loan.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse saveUser(UserDto userDto) {
        if (userRepo.existsUserByEmail(userDto.getEmail())
                || userRepo.existsUserByUsername(userDto.getUsername())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userMapper.toEntity(userDto);
        return userMapper.toResponse(userRepo.save(user));
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public LoginResponse loginUser(String username, String password) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.CREDENTIALS_INVALID);
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(token);
    }
}
