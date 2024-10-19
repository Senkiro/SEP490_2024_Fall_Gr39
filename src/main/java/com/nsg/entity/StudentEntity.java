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
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_id", columnDefinition = "VARCHAR(36)")
    String studentId;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "japanese_name")
    String japaneseName;

    @Column(name = "dob")
    Date dob;

    @Column(name = "gender")
    boolean gender;

    @Column(name = "avg_mark")
    float avgMark;

    //Relationship
    @OneToOne
            @MapsId
            @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
            @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    Class classEntity;

    @ManyToOne
            @JoinColumn(name = "batch_name", referencedColumnName = "batch_name")
    Batch batch;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<Attendence> attendenceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<Mark> markList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentEntity", fetch = FetchType.EAGER)
    List<EventFeedback> eventFeedbackList;

}
