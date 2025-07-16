package com.smartleave.controller;

import com.smartleave.dto.user.UserRequestDTO;
import com.smartleave.dto.user.UserResponseDTO;
import com.smartleave.dto.user.UserUpdateRequestDTO;
import com.smartleave.security.UserPrincipal;
import com.smartleave.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        return userService.getUserById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/me")
    public UserResponseDTO updateProfile(
            @RequestBody UserUpdateRequestDTO request,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        return userService.updateProfile(userId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public UserResponseDTO updateUserByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO request
    ) {
        return userService.updateUserByAdmin(id, request);
    }

    @GetMapping("/debug/roles")
public List<String> debugRoles(Authentication auth) {
    return auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
}

}

