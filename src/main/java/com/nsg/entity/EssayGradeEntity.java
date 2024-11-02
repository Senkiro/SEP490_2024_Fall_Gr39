package com.nsg.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "EssayGrade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EssayGradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "essay_grade_id", columnDefinition = "VARCHAR(36)")
    String essayGradeId;

    @Column(name = "comment")
    String comment;

    @Column(name = "grammar")
    int grammar;

    @Column(name = "vocabulary")
    int vocabulary;

    @Column(name = "fluency")
    int fluency;

    @Column(name = "understandability")
    int understandability;

    @Column(name = "average")
    float average;

    @OneToOne
    @MapsId
    @JoinColumn(name = "essay_id")
    EssayEntity essayEntity;
}
