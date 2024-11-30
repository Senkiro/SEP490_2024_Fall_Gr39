package com.nsg.repository;
import com.nsg.common.enums.UserRole;
import com.nsg.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TeacherRepository extends JpaRepository<UserEntity, String> {
    //get all teacher
    List<UserEntity> findByRoles(UserRole role);
    //get teacher with paginate
    Page<UserEntity> findByRoles(UserRole role, Pageable pageable);

    //get list of available teacher for each session
    @Query("SELECT u FROM UserEntity u WHERE u.roles = 'TEACHER' " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM SessionEntity s WHERE s.user.userId = u.userId " +
            "   AND s.date = :date " +
            "   AND s.timeSlotEntity.timeSlotId = :timeSlotId" +
            ")")
    List<UserEntity> findAvailableTeachers(@Param("date") LocalDate date,
                                           @Param("timeSlotId") String timeSlotId);

}