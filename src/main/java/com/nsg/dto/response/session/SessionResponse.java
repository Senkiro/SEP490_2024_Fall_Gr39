package com.nsg.dto.response.session;

import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionResponse {
    String sessionId;

    LocalDate date;

    boolean status;

    LessonResponse lessonResponse;

    TimeSlotResponse timeSlotResponse;

    String roomName;

    ExamResponse examResponse;

    String eventName;

    String userId;

}
