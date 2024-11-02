package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "News")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "news_id", columnDefinition = "VARCHAR(36)")
    String newId;

    @Column(name = "news_title")
    String newTitle;

    @Column(name = "news_content")
    String newContent;

    @Column(name = "created_at")
    Date createdAt;

}
