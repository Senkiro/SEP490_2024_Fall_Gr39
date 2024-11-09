package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "Chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chapter_id", columnDefinition = "VARCHAR(36)")
    String chapterId;

    @Column(name = "chapter_title")
    String chapterTitle;

    @OneToMany(mappedBy = "chapterEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<LessonEntity> lessonEntityList;

    @OneToMany(mappedBy = "chapterEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<ExamEntity> examEntityList;
}
