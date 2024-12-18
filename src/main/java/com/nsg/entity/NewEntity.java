package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "News")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "news_id", columnDefinition = "VARCHAR(36)")
    String newId;

    @Column(name = "news_title")
    String newTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT",name = "news_content")
    String newContent;

    @Column(name = "status")
    boolean status;

}
