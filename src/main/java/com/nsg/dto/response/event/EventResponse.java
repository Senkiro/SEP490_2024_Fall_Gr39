package com.nsg.dto.response.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
