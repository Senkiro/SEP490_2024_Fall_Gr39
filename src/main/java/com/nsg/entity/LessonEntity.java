package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    int lessonId;

    @Column(name = "lesson_title")
    String lessonTitle;

    @Lob
    @Column(name = "vocabulary")
    String vocabulary;

    @Lob
    @Column(name = "kanji")
    String kanji;

    @Lob
    @Column(name = "grammar")
    String grammar;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lessonEntity", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

    @OneToOne(mappedBy = "lessonEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    CurriculumnEntity curriculumnEntity;


}
