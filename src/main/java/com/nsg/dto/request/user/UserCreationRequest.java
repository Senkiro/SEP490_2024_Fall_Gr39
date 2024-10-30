package com.nsg.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
//    private String firstName;
//    private String lastName;
    @Size(min = 3, message = "Username must be at least 3 characters!")
    @NotNull
    private String username;
//    private String img;
//
//    @DateTimeFormat
//    private LocalDate dob;
//    private boolean gender;
//    private String address;

    @Size(min = 8, message = "Password must be at least 8 characters!")
    @NotNull
    private String password;

    @Email
    private String email;

    private String role;
}
