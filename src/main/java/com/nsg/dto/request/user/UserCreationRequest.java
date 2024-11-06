package com.nsg.dto.request.user;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.*;
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
@MappedSuperclass
public class UserCreationRequest {
    @Size(min = 3, message = "INVALID_FULLNAME")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "INVALID_FULLNAME")
    String fullName;

    @Size(min = 3, message = "INVALID_JAPANESENAME")
    String japaneseName;

    @Email(message = "INVALID_EMAIL")
    @NotNull(message = "NOTNULL_EMAIL")
    String email;

    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    @Pattern(regexp="(^$|[0-9]{10})", message = "INVALID_PHONE_NUMBER")
    String phone;

    @NotNull(message = "GENDER_NOTNULL")
    boolean gender;
}
