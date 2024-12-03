package com.nsg.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInforResponse {
    String userId;
    String fullName;
    String japaneseName;
    LocalDate dob;
    String img;
    boolean gender;
    String email;
    String phone;
}
