package com.smartleave.controller;

import com.smartleave.dto.auth.AuthResponseDTO;
import com.smartleave.dto.auth.LoginRequestDTO;
import com.smartleave.dto.auth.RegisterRequestDTO;
import com.smartleave.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }
}
