package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "LessonEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonEntity {
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonEntity", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

}
