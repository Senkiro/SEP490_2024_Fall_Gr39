package com.nsg.dto.request.news;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Lob
    @Size(max = Integer.MAX_VALUE)
    String newContent;

    boolean status;

    String createdBy;
}
