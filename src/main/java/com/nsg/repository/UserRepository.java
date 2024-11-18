package com.nsg.repository;

import com.nsg.common.enums.UserRole;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    //check username existed
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserEntity> getByRoles(UserRole role);

    Page<UserEntity> findByFullNameContaining(String name, PageRequest of);

//    public Optional<User> findByUserId(String userId);

}
