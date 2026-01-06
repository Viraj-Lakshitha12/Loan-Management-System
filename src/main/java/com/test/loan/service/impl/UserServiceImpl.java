package com.test.loan.service.impl;

import com.test.loan.advisor.BusinessException;
import com.test.loan.dto.common.ErrorCode;
import com.test.loan.dto.request.UserDto;
import com.test.loan.dto.response.UserResponse;
import com.test.loan.entity.User;
import com.test.loan.repo.UserRepo;
import com.test.loan.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponse saveUser(UserDto userDto) {
        if (userRepo.existsUserByEmail(userDto.getEmail())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }
        if (userRepo.existsUserByUsername(userDto.getUsername())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }
        User save = userRepo.save(modelMapper.map(userDto, User.class));
        return modelMapper.map(save, UserResponse.class);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return modelMapper.map(user, UserResponse.class);
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

}
