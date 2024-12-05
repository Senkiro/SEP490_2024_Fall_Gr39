package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_id", columnDefinition = "VARCHAR(36)")
    String sessionId;

    @Column(name = "session_number")
    int sessionNumber;

    @Column(name = "session_week")
    int sessionWeek;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "status")
    boolean status;

    @Column(name = "session_available")
    boolean sessionAvailable;

    @Column(name = "note")
    String note;

    //Added, Not added, Not happen
    @Column(name = "mark_status")
    String markStatus;

    //Taken, Not taken, Not happen
    @Column(name = "attendance_status")
    String attendanceStatus;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    ClassEntity classEntity;

    @ManyToOne
    @JoinColumn(name = "curriculumnId", referencedColumnName = "lesson_id")
    CurriculumnEntity curriculumnEntity;

    @OneToMany(mappedBy = "sessionEntity", orphanRemoval = true)
    List<AttendanceEntity> attendanceEntityList;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    TimeSlotEntity timeSlotEntity;

    @ManyToOne
    @JoinColumn(name = "room_id")
    RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name = "event_id")
    EventEntity eventEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

}
