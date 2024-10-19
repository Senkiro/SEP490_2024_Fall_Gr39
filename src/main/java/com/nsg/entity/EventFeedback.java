package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "EventFeedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_feedback_id", columnDefinition = "VARCHAR(36)")
    String eventFeedbackId;


    @Column(name = "feedback_grade")
    String feedbackGrade;

    @Column(name = "feedback_content")
    String feedbackContent;

}