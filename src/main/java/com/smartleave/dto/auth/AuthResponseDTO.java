package com.smartleave.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";
}
