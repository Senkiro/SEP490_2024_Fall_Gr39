package com.nsg.dto.request.session;

import com.nsg.entity.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionCreattionRequest {
    LocalDate date;
    boolean status;
    int sessionNumber;
    int sessionWeek;

    Integer curriculumnId;

    String timeSlotId;

    String roomNumber;

    String classId;

    String eventId;
    String userId;

    boolean sessionAvailable;

    String note;

    String markStatus;

    String attendanceStatus;
}
