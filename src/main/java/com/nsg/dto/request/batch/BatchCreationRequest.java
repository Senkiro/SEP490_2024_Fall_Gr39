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

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchCreationRequest {
    @NotNull(message = "INVALID_BATCHNAME")
    @NotBlank(message = "INVALID_BATCHNAME")
    String batchName;

    @FutureOrPresent(message = "End time must be in the present or future")
    Date endTime;

    @NotNull(message = "Year can not be null!")
    @Min(value = 2000, message = "Year must be after 2000")
    int year;
}
