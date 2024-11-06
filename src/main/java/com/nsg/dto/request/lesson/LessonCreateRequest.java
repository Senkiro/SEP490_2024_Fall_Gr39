package com.nsg.dto.request.lesson;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonCreateRequest {
    @NotNull(message = "NOTNULL_LESSON_TITLE")
    String lessonTitle;

    String lessonContent;

}
