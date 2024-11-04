package com.nsg.dto.response.staff;

import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.StudentEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
    String rollNumber;
    UserInforResponse user;
}
