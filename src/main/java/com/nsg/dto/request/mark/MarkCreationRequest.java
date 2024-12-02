package com.nsg.dto.request.mark;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarkCreationRequest {
    @NotNull(message = "MARK_NOT_NULL")
    @DecimalMin(value = "0.0", inclusive = true, message = "MIN_VALUE_MARK")
    @DecimalMax(value = "10.0", inclusive = true, message = "MAX_VALUE_MARK")
    Float mark;

    //can be null
    String comment;

    //can
    boolean status;

    //not null
    @NotBlank
    String studentId;

    int examId;
}
