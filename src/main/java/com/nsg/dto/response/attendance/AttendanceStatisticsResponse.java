package com.nsg.dto.response.attendance;

import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.student.StudentResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceStatisticsResponse {
    long attendCount;
    long totalCount;
    double attendPercentage;
}
