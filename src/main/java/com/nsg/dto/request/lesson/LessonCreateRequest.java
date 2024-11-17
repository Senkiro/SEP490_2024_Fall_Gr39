package com.nsg.dto.request.lesson;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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

    @NotNull(message = "VOCABULARY_IS_NULL")
    String vocabulary;

    @NotNull(message = "KANJI_IS_NULL")
    String kanji;

    String grammar;

}
