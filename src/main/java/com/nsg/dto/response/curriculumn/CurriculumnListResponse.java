package com.nsg.dto.response.curriculumn;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurriculumnListResponse {
    int curriculumnListId;

    String curriculumnTitle;
}
