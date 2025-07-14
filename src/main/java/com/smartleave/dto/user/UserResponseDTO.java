package com.smartleave.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
}
