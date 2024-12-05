package com.nsg.dto.response.eventFeedback;

import com.nsg.dto.response.event.EventResponse;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.EventEntity;
import com.nsg.entity.StudentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFeedbackResponse {
    String eventFeedbackId;

    int feedbackRate;

    String feedbackContent;

    EventResponse eventResponse;

    StudentResponse studentResponse;
}
