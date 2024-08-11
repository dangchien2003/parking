package com.parking.identity_service.configuration;

import com.parking.identity_service.entity.Role;
import com.parking.identity_service.entity.User;
import com.parking.identity_service.exception.AppException;
import com.parking.identity_service.exception.ErrorCode;
import com.parking.identity_service.repository.RoleRepository;
import com.parking.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            String emailAdmin = "admin@gmail.com";
            if (userRepository.findByEmail(emailAdmin).isEmpty()) {

                Role role = roleRepository.findById("ADMIN").orElseThrow(() ->
                        new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

                HashSet<Role> roles = new HashSet<>();
                roles.add(role);

                User user = User.builder()
                        .email(emailAdmin)
                        .roles(roles)
                        .password(passwordEncoder.encode("admin"))
                        .build();

                userRepository.save(user);
                log.warn("admin user has been create");
            }
        };
    }
}
