package com.nsg.dto.response.student;

import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.user.UserInforResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {

    //id, rollnumber
    String studentId;
    String rollNumber;

    //user information
    UserInforResponse userInforResponse;

    //batch
    String batchName;

    //class
    ClassResponse classResponse;
}
