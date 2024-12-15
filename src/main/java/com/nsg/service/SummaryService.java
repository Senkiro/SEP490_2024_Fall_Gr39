package com.nsg.service;

import com.nsg.dto.response.attendance.DailyAttendanceAndMarksSummaryResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SummaryService {

    @Autowired
    MarkService markService;

    @Autowired
    AttendanceService attendanceService;

    public DailyAttendanceAndMarksSummaryResponse getDailyAttendanceAndMarksSummary(LocalDate date) {

        int totalAttendance = attendanceService.calculateAttendedStudentInDay( date);
        int totalMarked = markService.calculateTotalMarkedInDay( date);

        return DailyAttendanceAndMarksSummaryResponse.builder()
                .totalAttendance(totalAttendance)
                .totalMarked(totalMarked)
                .unMarked( totalAttendance - totalMarked )
                .build();
    }
}
