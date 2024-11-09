package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "Event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id", columnDefinition = "VARCHAR(36)")
    String eventId;

    @Column(name = "event_name")
    String eventName;

    @Column(name = "address")
    String address;

    @Column(name = "img")
    private String imagePath;

    @Column(name = "status")
    boolean status;

    @Column(name = "description")
    String description;

    @Column(name = "avg_rate")
    float avgRate;

    @OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

    @OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<EventFeedbackEntity> eventFeedbackEntityList;

}
