package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    int sessionId;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "status")
    boolean status;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id")
    LessonEntity lessonEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessionEntity", fetch = FetchType.EAGER)
    List<AttendenceEntity> attendenceEntityList;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    TimeSlotEntity timeSlotEntity;

    @ManyToOne
    @JoinColumn(name = "room_id")
    RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    ExamEntity examEntity;

    @ManyToOne
    @JoinColumn(name = "event_id")
    EventEntity eventEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

}
