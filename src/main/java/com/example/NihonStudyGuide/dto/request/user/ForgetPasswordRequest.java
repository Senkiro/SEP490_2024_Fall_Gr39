package com.example.NihonStudyGuide.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgetPasswordRequest {
    @Email(message = "INVALID_EMAIL")
    String email;
}
