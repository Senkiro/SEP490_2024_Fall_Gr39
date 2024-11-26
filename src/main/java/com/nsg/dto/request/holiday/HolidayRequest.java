package com.nsg.dto.request.holiday;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HolidayRequest {
    @NotNull(message = "HOLIDAYTITLE_IS_NULL")
    String title;

    LocalDate holidayDate;
}
