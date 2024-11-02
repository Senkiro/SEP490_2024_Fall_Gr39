package com.nsg.dto.request.batch;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 4, max = 10, message = "Batch name must be between 4 and 10 character")
    String batchName;

//    @FutureOrPresent(message = "Start time must be in the present or future")
    LocalDate startTime;

//    @FutureOrPresent(message = "End time must be in the present or future")
    LocalDate endTime;

    @NotNull(message = "Year can not be null!")
    @Min(value = 2000, message = "Year must be after 2000")
    int year;
}
