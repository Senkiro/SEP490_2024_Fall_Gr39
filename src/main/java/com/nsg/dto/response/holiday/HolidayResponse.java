package com.nsg.dto.response.holiday;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HolidayResponse {
    String holidayId;

    String title;

    LocalDate holidayDate;

}

