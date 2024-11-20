package com.nsg.dto.response.news;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsResponse {
    String newId;

    String newTitle;

    String newContent;

    boolean status;
}
