package com.nsg.config.corsconfig;


import com.nsg.common.enums.UserRole;
import com.nsg.entity.UserEntity;
import com.nsg.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByUsername("adminEntity").isEmpty()){
                UserEntity user = UserEntity.builder()
                        .username("adminEntity")
                        .password(passwordEncoder.encode("admin1"))
                        .email("admin01@gmail.com")
                        .roles(UserRole.ADMIN)
                        .isActive(true)
                        .build();
                userRepository.save(user);
                log.warn("adminEntity user has been created !");
            }
        };
    }
}
