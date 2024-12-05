package com.nsg.dto.request.eventFeedback;

import com.nsg.entity.EventEntity;
import com.nsg.entity.StudentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFeedbackCreattionRequest {
    @NotNull(message = "EVENT_FEED_BACK_RATE_IS_NULL")
    @Min(message = "Feedback rate minimum is 1", value = 1)
    @Max(message = "Feedback rate have max is 5.", value = 5)
    int feedbackRate;

    String feedbackContent;

    String eventId;

    String studentId;
}
