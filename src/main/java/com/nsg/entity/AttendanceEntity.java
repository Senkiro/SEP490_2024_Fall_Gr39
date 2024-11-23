package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attendance_id", columnDefinition = "VARCHAR(36)")
    String attendanceId;

    @Column(name = "status")
    String status;

    @Column(name = "note")
    String note;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    SessionEntity sessionEntity;

    @ManyToOne
    @JoinColumn(name = "student_id")
    StudentEntity studentEntity;
}
