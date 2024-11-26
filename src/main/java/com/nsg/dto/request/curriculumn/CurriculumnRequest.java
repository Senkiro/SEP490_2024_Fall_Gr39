package com.nsg.dto.request.curriculumn;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurriculumnRequest {
    int sessionNo;

    String lessonId;

    String examId;

    int curriculumnListId;
}
