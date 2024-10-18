package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lesson_id", columnDefinition = "VARCHAR(36)")
    String lessonId;

    @Column(name = "lesson_title")
    String lessonTitle;

    @Column(name = "lesson_content")
    String lessonContent;

    @Column(name = "created_at")
    Date createdAt;

}
