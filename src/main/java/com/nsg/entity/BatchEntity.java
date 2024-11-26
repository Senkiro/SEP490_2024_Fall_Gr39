package com.nsg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchEntity extends BaseEntity {
    @Id
    @Column(name = "batch_name")
    String batchName;

    @Column(name = "start_time", columnDefinition = "DATE")
    LocalDate startTime;

    @Column(name = "end_time", columnDefinition = "DATE")
    LocalDate endTime;

    @Column(name = "year")
    int year;

    @Column(name = "total_week")
    Integer totalWeek;

    @Column(name = "batch_status", columnDefinition = "TINYINT")
    Integer batchStatus;

    @JsonIgnore
    @OneToMany( mappedBy = "batchEntity", fetch = FetchType.EAGER)
    List<StudentEntity> studentEntityList;

    @OneToMany(mappedBy = "batchEntity", fetch = FetchType.EAGER)
    List<ClassEntity> classEntityList;
}
