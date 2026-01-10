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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse saveUser(UserDto userDto) {
        if (userRepo.existsUserByEmail(userDto.getEmail())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }
        if (userRepo.existsUserByUsername(userDto.getUsername())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }
        User userMapperEntity = userMapper.toEntity(userDto);
        userMapperEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User save = userRepo.save(userMapperEntity);
        return userMapper.toResponse(save);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    @Override
    public LoginResponse loginUser(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.CREDENTIALS_INVALID);
        }
        return new LoginResponse(
                jwtUtil.generateToken(user.getUsername(), user.getRole().name())
        );
    }

}
