package com.nsg.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 3, message = "INVALID_FULLNAME")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "INVALID_FULLNAME")
    String fullName;

    @Size(min = 3, message = "INVALID_JAPANESENAME")
    String japaneseName;

    @Email(message = "INVALID_EMAIL")
    @NotNull(message = "NOTNULL_EMAIL")
    String email;


    LocalDate dob;

    boolean gender;
}
