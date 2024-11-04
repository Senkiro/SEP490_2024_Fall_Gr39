package com.nsg.repository;

import com.nsg.common.enums.UserRole;
import com.nsg.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<UserEntity, String> {
    //get all teacher
    List<UserEntity> findByRoles(UserRole role);

    //get teacher with paginate
    Page<UserEntity> findByRoles(UserRole role, Pageable pageable);

}
