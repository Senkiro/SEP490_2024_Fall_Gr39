package com.nsg.dto.response.news;

import com.nsg.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

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

    Date createDate;

    Date updateDate;

    private String createdBy;

}

