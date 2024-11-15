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

    String lessionId;

    String timeSlotId;

    String roomName;

    String examId;

    String eventId;
    String userId;
}
