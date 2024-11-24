package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Curriculumn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurriculumnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculumn_id")
    int curriculumnId;

    @Column(name = "session_number")
    int sessionNumber;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    LessonEntity lessonEntity;

    @OneToOne
    @JoinColumn(name = "exam_id")
    ExamEntity examEntity;

    @ManyToOne
    @JoinColumn(name = "curriculumn_list_id")
    CurriculumnListEntity curriculumnListEntity;
}
