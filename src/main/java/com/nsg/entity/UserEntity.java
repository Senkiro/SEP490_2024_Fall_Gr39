package com.nsg.entity;

import com.nsg.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
    @Column(name = "username")
    private String username;
//
//    @Column(name = "img")
//    private String img;
//
//    @Column(name = "dob")
//    private LocalDate dob;
//
//    @Column(name = "gender")
//    private boolean gender;
//
//    @Column(name = "address")
//    private String address;

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
