package com.nsg.dto.response.exam;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamTypeResponse {
    Integer examType;

    String examRate;

    String examCategory;
}
