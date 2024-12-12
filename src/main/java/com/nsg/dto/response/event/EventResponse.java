package com.nsg.dto.response.event;

import com.nsg.dto.response.session.SessionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    String eventId;
    String eventName;
    String address;
    private String imagePath;
    boolean status;
    String description;
    Float avgRate;

    String sessionId;
    LocalDate eventDate;

}
