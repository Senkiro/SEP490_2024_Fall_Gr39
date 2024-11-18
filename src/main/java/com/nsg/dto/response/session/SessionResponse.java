package com.nsg.dto.response.session;

import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionResponse {
    String sessionId;

    int sessionNumber;
    int sessionWeek;
    DayOfWeek dayOfWeek;

    LocalDate date;

    boolean status;

    ClassResponse classResponse;

    LessonResponse lessonResponse;

    TimeSlotResponse timeSlotResponse;

    String roomNumber;

    ExamResponse examResponse;

    String eventName;

    String fullName;
    String email;

}
