package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    int examId;

    @Column(name = "exam_title")
    String examTitle;

    @Lob
    @Column(name = "exam_content", columnDefinition = "LONGTEXT")
    String examContent;

    @OneToMany(mappedBy = "examEntity", fetch = FetchType.EAGER)
    List<MarkEntity> markEntityList;

    @ManyToOne
    @JoinColumn(name = "exam_type")
    ExamTypeRateEntity examTypeRateEntity;

    @OneToOne(mappedBy = "examEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    CurriculumnEntity curriculumnEntity;

}
