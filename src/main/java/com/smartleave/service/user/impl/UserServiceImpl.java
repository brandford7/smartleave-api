package com.smartleave.service.user.impl;

import com.smartleave.dto.user.UserRequestDTO;
import com.smartleave.dto.user.UserResponseDTO;
import com.smartleave.dto.user.UserUpdateRequestDTO;
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
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return toDto(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request){
                boolean emailExists= userRepository.existsByEmail(request.getEmail());
                boolean userNameExists= userRepository.existsByEmail(request.getUsername());

                if (userNameExists || emailExists) {
                    throw new RuntimeException("Username or email exists");
                }

                  // Resolve role names to Role entities
    Set<Role> roleEntities = new HashSet<>();
    for (String roleName : request.getRoles()) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        roleEntities.add(role);
    }

                // Build User entity
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
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
if (request.getUsername() != null) user.setUsername(request.getUsername());


        User updatedUser = userRepository.save(user);
        return toDto(updatedUser);
    }

 @Override
public UserResponseDTO updateUserByAdmin(Long id, UserUpdateRequestDTO request) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    if (request.getUsername() != null) user.setUsername(request.getUsername());

    if (request.getRoles() != null && !request.getRoles().isEmpty()) {
        Set<Role> roles = request.getRoles().stream()
            .map(roleName -> roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Invalid role: " + roleName)))
            .collect(Collectors.toSet());
        user.setRoles(roles);
    }

    User savedUser = userRepository.save(user);
    return toDto(savedUser);
}

    @Override
    public boolean deleteUser(Long id) {
        boolean userExists = userRepository.existsById(id);

        if (!userExists) {
            throw new RuntimeException("User not found with ID: " + id);
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
