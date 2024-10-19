package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "exam_id", columnDefinition = "VARCHAR(36)")
    String examId;

    @Column(name = "exam_title")
    String examTitle;

    @Column(name = "exam_content")
    String examContent;

    @Column(name = "created_at")
    Date createdAt;
}
