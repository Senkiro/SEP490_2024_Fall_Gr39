package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_id", columnDefinition = "VARCHAR(36)")
    String studentId;

    @Column(name = "avg_mark")
    float avgMark;

    @Column(name = "roll_number")
    String rollNumber;

    //Relationship
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "batch_name", referencedColumnName = "batch_name")
    BatchEntity batchEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<AttendenceEntity> attendenceEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<MarkEntity> markEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<EventFeedbackEntity> eventFeedbackEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<EssayEntity> essayEntityList;

}
