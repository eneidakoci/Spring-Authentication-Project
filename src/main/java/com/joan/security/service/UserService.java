package com.joan.security.service;

import com.joan.security.dto.SignUpDTO;
import com.joan.security.dto.User;
import com.joan.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    private AuthenticationService authenticationService;

    public User signUp(SignUpDTO signUpDTO) {
        User user = new User(1L, signUpDTO.getLogin(), "token", "ROLE_USER", signUpDTO.getPassword());
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticationService.findByLogin(username);
    }
}
