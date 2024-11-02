package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Essay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EssayEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "essay_id", columnDefinition = "VARCHAR(36)")
    String essayId;

    @Column(name = "essay_title")
    String essayTitle;

    @Column(name = "essay_content")
    String essayContent;

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

    @ManyToOne
    @JoinColumn(name = "student_id")
    StudentEntity studentEntity;

//    @OneToOne(mappedBy = "essayEntity", cascade = CascadeType.ALL)
//    EssayGradeEntity essayGradeEntity;

}
