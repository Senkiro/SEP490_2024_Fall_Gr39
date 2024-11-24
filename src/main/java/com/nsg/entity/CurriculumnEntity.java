package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @OneToMany(mappedBy = "chapterEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<LessonEntity> lessonEntityList;

    @OneToMany(mappedBy = "chapterEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<ExamEntity> examEntityList;

    @ManyToOne
    @JoinColumn(name = "curriculumn_list_id")
    CurriculumnListEntity curriculumnListEntity;
}
