package com.joan.security.mapper;

import com.joan.security.dto.User;
import com.joan.security.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setToken(user.getToken());
        userDTO.setAuthorities(String.valueOf(user.getAuthorities()));
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setToken(userDTO.getToken());
        user.setAuthorities(userDTO.getAuthorities());
        return user;
    }
}