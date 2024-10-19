package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "ExamTypeRate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamTypeRate {
    @Id
    @Column(name = "exam_type")
    Integer examType;

    @Column(name = "exam_rate")
    String examRate;

    @Column(name = "exam_name")
    Date examName;

}