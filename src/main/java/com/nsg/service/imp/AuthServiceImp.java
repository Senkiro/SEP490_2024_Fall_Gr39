package com.nsg.service.imp;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.user.AuthRequest;
import com.nsg.dto.request.user.RegisterRequest;
import com.nsg.dto.response.user.AuthResponse;
import com.nsg.dto.response.user.RegisterResponse;
import com.nsg.entity.UserEntity;
import com.nsg.repository.UserRepository;
import com.nsg.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImp implements AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @org.springframework.beans.factory.annotation.Value("${signer.key}")
    protected String SIGNER_KEY;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    //login
    private Map<String, Integer> failedLoginAttemptsMap = new HashMap<>();

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(user.isActive()) {
            boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
            int maxAttempts = 4;
            if (!authenticated) {
                int currentFailedAttempts = failedLoginAttemptsMap.getOrDefault(user.getUsername(), 0);
                currentFailedAttempts++;
                failedLoginAttemptsMap.put(user.getUsername(), currentFailedAttempts);
                if (currentFailedAttempts < maxAttempts) {
                    throw new AppException(ErrorCode.INVALID_LOGINRQ);
                } else {
                    user.setActive(false);
                    userRepository.save(user);
                    throw new AppException(ErrorCode.INVALID_ACCBAN);
                }
            } else {
                failedLoginAttemptsMap.remove(user.getUsername());
            }
            var token = generateToken(user);
            return AuthResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .scope(user.getRoles().name())
                    .build();
        }else {
            throw new AppException(ErrorCode.INVALID_ACCBAN);
        }
    }

    //register
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//<<<<<<< Updated upstream
////        user.setRoles();
//=======
//        user.setRole("CUSTOMER");
//>>>>>>> Stashed changes
        user.setActive(true);
        UserEntity savedUser = userRepository.save(user);
        RegisterResponse response = new RegisterResponse();
        BeanUtils.copyProperties(savedUser, response);
        return response;
    }

    //gen token
    private String generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .claim("id", user.getUserId())
                .subject(user.getUsername())
                .claim("scope", user.getRoles().name())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
}
