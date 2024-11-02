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
public class EssayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "essay_id", columnDefinition = "VARCHAR(36)")
    String essayId;

    @Column(name = "essay_title")
    String essayTitle;

    @Column(name = "essay_content")
    String essayContent;

    @ManyToOne
    @JoinColumn(name = "student_id")
    StudentEntity studentEntity;

    @OneToOne(mappedBy = "essayEntity", cascade = CascadeType.ALL)
    EssayGradeEntity essayGradeEntity;

}
