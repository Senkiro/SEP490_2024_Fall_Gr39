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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lesson_id", columnDefinition = "VARCHAR(36)")
    String lessonId;

    @Column(name = "lesson_title")
    String lessonTitle;

//    @Column(name = "lesson_content")
//    String lessonContent;

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

}
