package com.nsg.dto.request.event;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateRequest {
    @NotBlank(message = "NOTNULL_EVENT_TITLE")
    String eventName;

    @NotBlank(message = "NOTNULL_EVENT_ADDRESS")
    String address;

    String description;
}
