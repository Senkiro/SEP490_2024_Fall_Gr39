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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "exam_id", columnDefinition = "VARCHAR(36)")
    String examId;

    @Column(name = "exam_title")
    String examTitle;

    @Column(name = "exam_content")
    String examContent;

    @OneToMany(mappedBy = "examEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

    @OneToMany(mappedBy = "examEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<MarkEntity> markEntityList;

    @ManyToOne
    @JoinColumn(name = "exam_type")
    ExamTypeRateEntity examTypeRateEntity;
}
