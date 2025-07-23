package com.example.bankcards.service;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.customExceptions.userExceptions.UserAlreadyBlockedException;
import com.example.bankcards.exception.customExceptions.userExceptions.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserService(
            final UserRepository userRepository,
            final RoleRepository roleRepository,
            final UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    @Transactional
    public void blockUser(final Long userId)
            throws UserNotFoundException, UserAlreadyBlockedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserAlreadyBlockedException(userId);
        }

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserRoles(final Long userId, final Set<String> roleNames)
            throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Set<Role> roles = roleNames.stream()
                .map(roleRepository::findByName).collect(Collectors.toSet());

        roles.forEach(user::addAuthority);
        userRepository.save(user);
    }
}