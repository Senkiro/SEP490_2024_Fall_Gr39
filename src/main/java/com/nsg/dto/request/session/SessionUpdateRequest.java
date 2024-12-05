package com.nsg.dto.request.session;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionUpdateRequest {
    Integer curriculumnId;

    String roomNumber;

    String eventId;

    String userId;

    boolean sessionAvailable;

    String note;

    String markStatus;

    String attendanceStatus;
}
