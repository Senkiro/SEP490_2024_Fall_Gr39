package com.nsg.dto.response.mark;

import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.student.StudentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarkResponse {
    String markId;

    float mark;

    String comment;

    boolean status;

    StudentResponse studentResponse;

    ExamResponse examResponse;
}
