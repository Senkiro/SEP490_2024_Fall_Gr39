package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_id", columnDefinition = "VARCHAR(36)")
    String classId;

    @Column(name = "class_name")
    String className;

    @Column(name = "class_colour")
    Date classColour;
}