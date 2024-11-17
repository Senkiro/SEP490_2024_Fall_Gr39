package com.nsg.dto.request.student;

import com.nsg.dto.request.user.UserCreationRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCreattionRequest extends UserCreationRequest {
    @Pattern(regexp="(^$|[A-Z0-9]{8})", message = "Invalid roll number!")
    String rollNumber;
    String batchName;
    String className;
}
