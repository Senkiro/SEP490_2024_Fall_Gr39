package com.nsg.dto.request.attendance;

import com.nsg.entity.SessionEntity;
import com.nsg.entity.StudentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceRequest {

    String status;

    String note;

    String sessionId;

    String studentId;

    public AttendanceRequest(String defaultStatus, String sessionId, String studentId) {
        this.status = defaultStatus;
        this.sessionId = sessionId;
        this.studentId = studentId;
    }
}
