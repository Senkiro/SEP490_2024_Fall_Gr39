package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BatchEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchEntity {
    @Id
    @Column(name = "batch_name")
    String batchName;

    @Column(name = "start_time")
    Date startTime;

    @Column(name = "end_time")
    Date endTime;

    @Column(name = "year")
    int year;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "batchEntity", fetch = FetchType.EAGER)
    List<StudentEntity> studentEntityList;
}
