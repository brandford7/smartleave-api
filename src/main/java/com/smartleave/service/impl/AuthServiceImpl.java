package com.smartleave.service.impl;

import com.smartleave.dto.auth.LoginRequestDTO;
import com.smartleave.dto.auth.RegisterRequestDTO;
import com.smartleave.dto.auth.AuthResponseDTO;
import com.smartleave.model.Role;
import com.smartleave.model.User;
import com.smartleave.repository.RoleRepository;
import com.smartleave.repository.UserRepository;
import com.smartleave.service.AuthService;
import com.smartleave.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        boolean userNameExists = userRepository.existsByUsername(request.getUsername());
        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if (userNameExists || emailExists) {
            throw new RuntimeException("Username or Email already exists");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(userRole))
                .build();

        userRepository.save(user);

        UserDetails userDetails = mapToUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = mapToUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .build();
    }

    private UserDetails mapToUserDetails(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // always prefix with "ROLE_"
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
