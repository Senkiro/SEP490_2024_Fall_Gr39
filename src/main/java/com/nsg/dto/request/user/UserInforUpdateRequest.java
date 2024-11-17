package com.nsg.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInforUpdateRequest {
    @Size(min = 2, message = "INVALID_FULLNAME")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "INVALID_FULLNAME")
    String fullName;

    @Size(min = 2, message = "INVALID_JAPANESENAME")
    String japaneseName;

    @Email(message = "INVALID_EMAIL")
    @NotNull(message = "NOTNULL_EMAIL")
    String email;

    @Past(message = "DOB_NOT_IN_PAST")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;

    @Pattern(regexp="(^$|[0-9]{10})", message = "INVALID_PHONE_NUMBER")
    String phone;

    @NotNull(message = "GENDER_NOTNULL")
    boolean gender;

    String img;

}
