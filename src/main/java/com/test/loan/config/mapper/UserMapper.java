package com.test.loan.config.mapper;

import com.test.loan.dto.request.UserDto;
import com.test.loan.dto.response.UserResponse;
import com.test.loan.entity.User;
import com.test.loan.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // DTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "role", expression = "java(mapRole(dto.getRole()))")
    User toEntity(UserDto dto);

    // Entity → Response (no need to ignore password - it's not in UserResponse)
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserResponse toResponse(User user);

    // Helper method to convert String to Role enum
    default Role mapRole(String role) {
        return role != null ? Role.valueOf(role.toUpperCase()) : null;
    }
}