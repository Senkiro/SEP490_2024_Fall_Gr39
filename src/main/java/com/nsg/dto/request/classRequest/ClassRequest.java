package com.nsg.dto.request.classRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassRequest {

    @NotBlank(message = "CLASS_NAME_IS_NULL")
    String className;

    @NotBlank(message = "CLASS_COLOUR_IS_NULL")
    String classColour;

    String batchName;
}
