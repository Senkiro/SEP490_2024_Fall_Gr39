package com.nsg.repository;

import com.nsg.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessionRepository extends BaseRepository<SessionEntity, String> {
    List<SessionEntity> findBySessionWeekAndClassEntityClassId(int week, String classId);

    @Query("SELECT s FROM SessionEntity s WHERE s.user.id = :userId")
    List<SessionEntity> findByUserId(@Param("userId") String userId);

//    @Query("SELECT s FROM SessionEntity s WHERE s.classEntity.classId = :classId AND s.user.id = :userId")
//    List<SessionEntity> findByClassIdAndUserId(@Param("classId") String classId, @Param("userId") String userId);


    boolean existsByClassEntityClassId(String classId);

    List<SessionEntity> findByClassEntityClassId(String classId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Session WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSessionBySessionId(@Param("sessionId") String sessionId);

    //get session by: classId, session_available = true, status = false; sort by date and sessionNumber
    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND session_available = TRUE " +
            "AND status = FALSE " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<SessionEntity> findSessionsByClassIdAndAvailableAndStatus(
            @Param("classId") String classId);

    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND session_available = FALSE " +
            "AND status = FALSE " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<SessionEntity> findSessionsUnavailableByClassId(
            @Param("classId") String classId);

}
