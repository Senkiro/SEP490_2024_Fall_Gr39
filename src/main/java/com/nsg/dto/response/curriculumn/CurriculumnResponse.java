package com.nsg.dto.response.curriculumn;

import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurriculumnResponse {
    int curriculumnId;
    int sessionNumber;

    LessonResponse lessonResponse;

    ExamResponse examResponse;

    CurriculumnListResponse curriculumnListResponse;
}
