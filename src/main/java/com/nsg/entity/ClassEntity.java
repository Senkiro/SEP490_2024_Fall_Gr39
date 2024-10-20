package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ClassEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_id", columnDefinition = "VARCHAR(36)")
    String classId;

    @Column(name = "class_name")
    String className;

    @Column(name = "class_colour")
    Date classColour;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classEntity", fetch = FetchType.EAGER)
    List<StudentEntity> studentEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classEntity", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;
}
