package com.nsg.dto.request.exam;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamUpdateRequest {
    String examTitle;
    String examContent;
    int examType;
}
