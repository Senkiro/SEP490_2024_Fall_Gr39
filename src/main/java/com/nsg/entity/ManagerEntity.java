package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Manager")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "manager_id", columnDefinition = "VARCHAR(36)")
    String managerId;

    @Column(name = "name")
    String name;

    @Column(name = "email")
    String email;

}
