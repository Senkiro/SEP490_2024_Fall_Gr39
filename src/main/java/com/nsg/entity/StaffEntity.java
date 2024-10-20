package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "StaffEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "staff_id", columnDefinition = "VARCHAR(36)")
    String staffId;

    @Column(name = "name")
    String name;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @OneToOne
            @MapsId
            @JoinColumn(name = "user_id")
    UserEntity user;

}
