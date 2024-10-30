package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_id", columnDefinition = "VARCHAR(36)")
    String sessionId;

    @Column(name = "date")
    Date date;

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

    @OneToOne
            @MapsId
            @JoinColumn(name = "session_id")
    UserEntity userEntity;

}
