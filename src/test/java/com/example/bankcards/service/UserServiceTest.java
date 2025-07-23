package com.example.bankcards.service;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    @DisplayName("getAllUsers returns page")
    void getAllUsers() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new User())));
        when(userMapper.toDTO(any())).thenReturn(new UserDTO());
        Page<UserDTO> result = userService.getAllUsers(Pageable.unpaged());
        assertNotNull(result);
    }

    @Test
    @DisplayName("blockUser blocks user")
    void blockUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        assertDoesNotThrow(() -> userService.blockUser(1L));
    }

    @Test
    @DisplayName("updateUserRoles updates roles")
    void updateUserRoles() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Role role = new Role();
        when(roleRepository.findByName(any())).thenReturn(role);
        when(userRepository.save(any())).thenReturn(user);
        assertDoesNotThrow(() -> userService.updateUserRoles(1L, Set.of("USER")));
    }
} 