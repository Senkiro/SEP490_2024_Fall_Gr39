package com.nsg.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "TimeSlot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "time_slot_id", columnDefinition = "VARCHAR(36)")
    String timeSLotId;

    @Column(name = "name")
    String name;

    @Column(name = "start_time")
    Date startTime;

    Date endTime;

    Date createdAt;


}
