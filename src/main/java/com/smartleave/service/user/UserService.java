package com.smartleave.service.user;

import java.util.List;

import com.smartleave.dto.user.UserRequestDTO;
import com.smartleave.dto.user.UserResponseDTO;
import com.smartleave.dto.user.UserUpdateRequestDTO;


public interface UserService {
List<UserResponseDTO> getAllUsers();
UserResponseDTO createUser(UserRequestDTO request);
UserResponseDTO getUserById(Long id);

UserResponseDTO updateProfile(Long id, UserUpdateRequestDTO dto);
UserResponseDTO updateUserByAdmin(Long id, UserUpdateRequestDTO dto);
boolean deleteUser(Long id);

    
}