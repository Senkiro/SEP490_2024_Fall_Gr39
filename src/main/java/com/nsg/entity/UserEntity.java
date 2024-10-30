package com.nsg.entity;

import com.nsg.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    private String userId;

    @Column(name = "username")
    private String username;

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

    @Column(name = "avg_mark")
    float avgMark;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    UserRole roles;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    StudentEntity studentEntity;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    TeacherEntity teacherEntity;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    StaffEntity staffEntity;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    ManagerEntity managerEntity;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    AdminEntity adminEntity;

}
