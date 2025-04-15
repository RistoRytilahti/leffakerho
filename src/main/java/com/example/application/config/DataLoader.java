package com.example.application.config;

import com.example.application.data.Role;
import com.example.application.data.User;
import com.example.application.data.UserRepository;
import com.example.application.security.SecurityConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder; // Korjattu injektio

    public DataLoader(UserRepository userRepository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Tarkistetaan, onko käyttäjät jo olemassa, jos ei, luodaan ne
        if (userRepository.count() == 0) {
            // Luodaan "user" käyttäjä
            User user = new User();
            user.setUsername("user");
            user.setName("User");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setRoles(Set.of(Role.USER)); // Asetetaan rooli "USER"
            userRepository.save(user);

            // Luodaan "admin" käyttäjä
            User admin = new User();
            admin.setUsername("admin");
            admin.setName("Admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Set.of(Role.ADMIN)); // Asetetaan rooli "ADMIN"
            userRepository.save(admin);
        }
    }
}