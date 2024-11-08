package com.nsg.dto.request.room;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {
    @NotNull(message = "ROOMNUMBER_IS_NULL")
    String roomNumber;
}
