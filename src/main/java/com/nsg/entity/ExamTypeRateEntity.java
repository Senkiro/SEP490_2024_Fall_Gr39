package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ExamTypeRate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamTypeRateEntity {
    @Id
    @Column(name = "exam_type")
    Integer examType;

    @Column(name = "exam_rate")
    String examRate;

    @Column(name = "exam_category")
    String examName;

    @OneToMany(mappedBy = "examTypeRateEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<ExamEntity> examEntityList;

}
