package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id", columnDefinition = "VARCHAR(36)")
    String eventId;

    @Column(name = "event_name")
    String eventName;

    @Column(name = "address")
    String address;

    @Column(name = "status")
    boolean status;

    @Column(name = "description")
    String description;

}
