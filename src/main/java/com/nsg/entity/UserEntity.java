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

    @Column(name = "phone")
    String phone;

    @Column(name = "is_active")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    UserRole roles;

    //relationship
    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    SessionEntity sessionEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity", fetch = FetchType.EAGER)
    List<EventFeedbackEntity> eventFeedbackEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity", fetch = FetchType.EAGER)
    List<MarkEntity> markEntityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity", fetch = FetchType.EAGER)
    List<AttendenceEntity> attendenceEntityList;

    @ManyToOne
    @JoinColumn(name = "batch_name", referencedColumnName = "batch_name")
    BatchEntity batchEntity;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    ClassEntity classEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity", fetch = FetchType.EAGER)
    List<Essay> essayList;


}
