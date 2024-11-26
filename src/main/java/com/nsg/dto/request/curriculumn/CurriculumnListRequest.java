package com.nsg.dto.request.curriculumn;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurriculumnListRequest {
    @NotBlank(message = "CURRICULUMN_TITLE_NOT_NULL")
    String curriculumnTitle;
}
