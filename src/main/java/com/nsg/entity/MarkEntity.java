package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Mark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "mark_id", columnDefinition = "VARCHAR(36)")
    String markId;


    @Column(name = "mark")
    float mark;

    @Column(name = "comment")
    String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    @ManyToOne
            @JoinColumn(name = "exam_id")
    ExamEntity examEntity;

}
