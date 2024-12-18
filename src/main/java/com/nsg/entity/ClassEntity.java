package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_id", columnDefinition = "VARCHAR(36)")
    String classId;

    @Column(name = "class_name")
    String className;

    @Column(name = "class_colour")
    String classColour;

    @Column(name = "class_status")
    boolean classStatus;

    @OneToMany(mappedBy = "classEntity", fetch = FetchType.EAGER)
    List<StudentEntity> studentEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classEntity", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

    @ManyToOne
    @JoinColumn(name = "batch_name")
    BatchEntity batchEntity;

}
