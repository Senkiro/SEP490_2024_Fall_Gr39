package com.nsg.entity;

import com.nsg.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

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

    @Column(name = "full_name")
    String fullName;

    @Column(name = "japanese_name")
    String japaneseName;

    @Column(name = "dob")
    Date dob;

    @Column(name = "img")
    String img;

    @Column(name = "gender")
    boolean gender;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_active")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    UserRole roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    StudentEntity studentEntity;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    List<SessionEntity> sessionEntityList;

}
