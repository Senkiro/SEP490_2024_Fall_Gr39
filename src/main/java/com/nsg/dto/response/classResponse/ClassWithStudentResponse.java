package com.nsg.dto.response.classResponse;

import com.nsg.dto.response.student.StudentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassWithStudentResponse {
    String classId;

    String className;

    String classColour;

    //student list
    Page<StudentResponse> listStudent;
}
