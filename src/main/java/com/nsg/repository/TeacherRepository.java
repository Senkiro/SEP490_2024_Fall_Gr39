package com.nsg.repository;
import com.nsg.common.enums.UserRole;
import com.nsg.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface TeacherRepository extends JpaRepository<UserEntity, String> {
    //get all teacher
    List<UserEntity> findByRoles(UserRole role);
    //get teacher with paginate
    Page<UserEntity> findByRoles(UserRole role, Pageable pageable);

    @Query(value = """
            SELECT u 
            FROM UserEntity u 
            WHERE u.role = 'TEACHER' 
              AND u.userId NOT IN (
                  SELECT s.user.userId 
                  FROM SessionEntity s 
                  WHERE s.sessionId = :sessionId 
              )
            """)
    List<UserEntity> findAvailableTeachers(@Param("sessionId") String sessionId);



}