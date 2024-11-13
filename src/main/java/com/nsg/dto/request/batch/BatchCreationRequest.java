package com.nsg.dto.request.batch;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchCreationRequest {
    @NotNull(message = "INVALID_BATCHNAME")
    @NotBlank(message = "INVALID_BATCHNAME")
    @Size(min = 4, max = 10, message = "BATCHNAME_SIZE_INVALID")
    String batchName;

    @Future(message = "START_TIME_NOTIN_FUTURE")
    LocalDate startTime;

    @Future(message = "END_TIME_NOTIN_FUTURE")
    LocalDate endTime;

    @NotNull(message = "YEAR_IS_NULL")
    @Min(value = 2000, message = "YEAR_NOT_AFTER2000")
    int year;
}
