package com.nsg.dto.response.attendance;

import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.student.StudentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceResponse {
    String attendanceId;

    String status;

    String note;

    String sessionId;

    LocalDate date;

    StudentResponse studentResponse;

    SessionResponse sessionResponse;

    String teacher;
}
