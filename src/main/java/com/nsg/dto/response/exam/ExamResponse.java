package com.nsg.dto.response.exam;

import com.nsg.entity.ExamTypeRateEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResponse {
    String examId;

    String examTitle;

    String examContent;

    ExamTypeResponse examTypeRate;
}
