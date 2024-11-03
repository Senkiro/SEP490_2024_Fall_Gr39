package com.nsg.dto.request.email;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {
    String fromEmail;
    String toEmail;
    String subject;
    String body;
}
