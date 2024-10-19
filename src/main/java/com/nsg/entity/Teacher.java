package com.nsg.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "teacher_id", columnDefinition = "VARCHAR(36)")
    String teacherId;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "japanese_name")
    String japaneseName;

    @Column(name = "dob")
    Date dob;

    @Column(name = "gender")
    boolean gender;

    @Column(name = "phone")
    String phone;

    @OneToOne
            @MapsId
            @JoinColumn(name = "user_id")
    UserEntity user;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL)
    Session session;
}
