package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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

}
