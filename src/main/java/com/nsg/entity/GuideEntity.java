package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "GuideEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "guide_id", columnDefinition = "VARCHAR(36)")
    String guideId;

    @Column(name = "guide_title")
    String guideTitle;

    @Column(name = "guide_content")
    String guideContent;



}
