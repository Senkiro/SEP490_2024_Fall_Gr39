package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "CurriculumnList")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurriculumnListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculumn_list_id")
    int curriculumnListId;

    @Column(name = "curriculumnTitle")
    String curriculumnTitle;

    @OneToMany(mappedBy = "curriculumnListEntity", fetch = FetchType.EAGER)
    List<CurriculumnEntity> curriculumnEntityList;
}
