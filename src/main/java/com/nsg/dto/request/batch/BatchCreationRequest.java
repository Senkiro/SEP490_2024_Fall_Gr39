package com.nsg.dto.request.batch;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    Date startTime;

    Date endTime;

    int year;
}
