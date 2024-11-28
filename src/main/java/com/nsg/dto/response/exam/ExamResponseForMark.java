package com.nsg.dto.response.exam;

import com.nsg.dto.response.mark.MarkResponse;
import com.nsg.entity.MarkEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResponseForMark {
    int examId;

    String examTitle;

    String examContent;

    ExamTypeResponse examTypeRate;

    String examType;

    MarkResponse markResponse;
}
