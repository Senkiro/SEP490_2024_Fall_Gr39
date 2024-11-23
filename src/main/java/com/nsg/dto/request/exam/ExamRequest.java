package com.nsg.dto.request.exam;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamRequest {

    String examTitle;

    String examContent;

    int exam_type;
    
}
