package com.smartleave.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartleave.dto.auth.AuthResponseDTO;
import com.smartleave.dto.auth.LoginRequestDTO;
import com.smartleave.dto.auth.RegisterRequestDTO;
import com.smartleave.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> Login(@Valid @RequestBody LoginRequestDTO request) {

        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> Register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        
        return ResponseEntity.ok(response);
    }
    
}
