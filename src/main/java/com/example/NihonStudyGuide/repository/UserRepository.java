package com.example.NihonStudyGuide.repository;

import com.example.NihonStudyGuide.entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);

    //check username existed
    boolean existsByUsername(String username);

//    public Optional<User> findByUserId(String userId);

}
