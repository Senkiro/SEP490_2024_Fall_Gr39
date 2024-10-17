package com.nsg.repository;

import com.nsg.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);

    //check username existed
    boolean existsByUsername(String username);

//    public Optional<User> findByUserId(String userId);

}
