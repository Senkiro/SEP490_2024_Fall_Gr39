package com.nsg.dto.response.attendance;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyAttendanceAndMarksSummaryResponse {
    int totalAttendance;
    int totalMarked;
    int unMarked;
}
