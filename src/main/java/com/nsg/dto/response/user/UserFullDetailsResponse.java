package com.nsg.dto.response.user;

import com.nsg.common.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFullDetailsResponse {
    String userId;
    String fullName;
    String japaneseName;
    LocalDate dob;
    String img;
    boolean gender;
    String email;
    String phone;

    UserRole role;

    boolean isActive;

    String password;

}
