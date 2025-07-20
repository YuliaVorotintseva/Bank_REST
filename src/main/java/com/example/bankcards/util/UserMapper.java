package com.example.bankcards.util;

import com.example.bankcards.dto.AdminUserDTO;
import com.example.bankcards.entity.User;

public class UserMapper {
    public AdminUserDTO toDTO(final User user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getAuthorities());
        return dto;
    }
}
