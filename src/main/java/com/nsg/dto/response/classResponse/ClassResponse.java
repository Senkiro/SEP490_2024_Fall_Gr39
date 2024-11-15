package com.nsg.dto.response.classResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassResponse {
    String classId;

    String className;

    String classColour;

}
