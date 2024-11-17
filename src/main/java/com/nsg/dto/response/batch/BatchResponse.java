package com.nsg.dto.response.batch;

import com.nsg.dto.response.classResponse.ClassResponse;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchResponse {
    String batchName;

    LocalDate startTime;

    LocalDate endTime;

    int year;

    Integer batchStatus;

    ClassResponse classResponse;
}
