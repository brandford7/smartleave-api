package com.smartleave.service;

import com.smartleave.dto.auth.AuthResponseDTO;
import com.smartleave.dto.auth.LoginRequestDTO;
import com.smartleave.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
