package com.nsg.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TimeSlot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSlotEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "time_slot_id", columnDefinition = "VARCHAR(36)")
    String timeSlotId;

    @Column(name = "name")
    String name;

    @Column(name = "start_time")
    LocalTime startTime;

    @Column(name = "end_time")
    LocalTime endTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timeSlotEntity", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

}
