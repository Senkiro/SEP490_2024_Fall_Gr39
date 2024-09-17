package com.example.NihonStudyGuide.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    String firstName;
    String lastName;
    LocalDate dob;
    String username;
    String email;
    String phoneNumber;
}