package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "Room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id", columnDefinition = "VARCHAR(36)")
    String roomId;

    @Column(name = "room_number")
    String roomNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomEntity", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;
}
