package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Guide")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "guide_id", columnDefinition = "VARCHAR(36)")
    String guideId;

    @Column(name = "guide_title")
    String guideTitle;

    @Column(name = "guide_content")
    String guideContent;

    @Column(name = "created_at")
    Date createdAt;

    String created_by;
}
