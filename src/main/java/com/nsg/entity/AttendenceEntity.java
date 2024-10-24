package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "AttendenceEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attendence_id", columnDefinition = "VARCHAR(36)")
    String attendenceId;

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
