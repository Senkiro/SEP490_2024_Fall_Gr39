package com.nsg.dto.request.news;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsRequest {
    @NotNull(message = "NEWSTITLE_IS_NULL")
    String newTitle;

    String newContent;

    boolean status;

    String createdBy;
}
