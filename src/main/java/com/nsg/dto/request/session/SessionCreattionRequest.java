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

    String lessionId;
    String examId;

    String timeSlotId;

    String roomNumber;

    String className;

    String eventId;
    String userId;
}
