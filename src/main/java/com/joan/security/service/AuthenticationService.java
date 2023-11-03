package com.joan.security.service;

import com.joan.security.dto.CredentialsDTO;
import com.joan.security.dto.User;
import com.joan.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(CredentialsDTO credentialsDto) {
        User user = repository.findUserByLogin(credentialsDto.getLogin());
        String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap(user.getPassword()));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), encodedMasterPassword)) {
            return this.findByLogin(credentialsDto.getLogin());
        }
        throw new RuntimeException("Invalid password");
    }

    public User findByLogin(String login) {
//        if ("joan".equals(login)) {
//            return new UserDTO(1L, "Joan", "Janku", "joan", "token", List.of("ROLE_VIEWER", "ROLE_EDITOR"));
//        }
//        if ("john".equals(login)) {
//            return new UserDTO(1L, "John", "Doe", "john", "token", List.of("ROLE_VIEWER"));
//        }
//        throw new RuntimeException("Invalid login");
     return repository.findUserDTOByLogin(login);
    }

}
