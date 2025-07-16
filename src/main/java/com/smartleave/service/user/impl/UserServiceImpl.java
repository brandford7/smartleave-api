package com.smartleave.service.user.impl;

import com.smartleave.dto.user.UserRequestDTO;
import com.smartleave.dto.user.UserResponseDTO;
import com.smartleave.dto.user.UserUpdateRequestDTO;
import com.smartleave.exception.DuplicateResourceException;
import com.smartleave.exception.ResourceNotFoundException;
import com.smartleave.model.Role;
import com.smartleave.model.User;
import com.smartleave.repository.RoleRepository;
import com.smartleave.repository.UserRepository;
import com.smartleave.service.user.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return toDto(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Set<Role> roleEntities = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roleEntities.add(role);
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleEntities)
                .build();

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Override
    public UserResponseDTO updateProfile(Long id, UserUpdateRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);
        return toDto(updatedUser);
    }

    @Override
    public UserResponseDTO updateUserByAdmin(Long id, UserUpdateRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getUsername() != null) user.setUsername(request.getUsername());

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid role: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        return true;
    }

    private UserResponseDTO toDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
