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
public class Session {
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
    Class classEntity;

    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id")
    Lesson lesson;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session", fetch = FetchType.EAGER)
    List<Attendence> attendenceList;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    Exam exam;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @OneToOne
            @MapsId
            @JoinColumn(name = "session_id")
    Teacher teacher;

}
