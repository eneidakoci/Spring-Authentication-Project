package com.joan.security.repository;

import com.joan.security.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);

    User save(User user);

    User findUserDTOByLogin(String login);
}
