package com.smartleave.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartleave.dto.user.UserRequestDTO;
import com.smartleave.dto.user.UserResponseDTO;
import com.smartleave.dto.user.UserUpdateRequestDTO;
import com.smartleave.security.UserPrincipal;
import com.smartleave.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO createdUser = userService.createUser(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

    }

        @GetMapping 
        public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
            List<UserResponseDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }
        
        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        }

        @GetMapping("/users/me")
public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    Long userId = principal.getId();
    UserResponseDTO user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
}
        

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        
@PatchMapping("/users/me")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public ResponseEntity<UserResponseDTO> updateProfile(
    @RequestBody UserUpdateRequestDTO request,
    Authentication authentication
) {
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    Long userId = principal.getId();
    UserResponseDTO updatedUser = userService.updateProfile(userId, request);
    return ResponseEntity.ok(updatedUser);
}

 @PatchMapping("path/{id}")
 public ResponseEntity<UserResponseDTO> updateUserByAdmin(@PathVariable Long id,
                @Valid @RequestBody UserUpdateRequestDTO request) 
        {
        
          UserResponseDTO updatedUser=   userService.updateUserByAdmin(id, request);
            
          return ResponseEntity.ok(updatedUser);
        }
    }

    
    

