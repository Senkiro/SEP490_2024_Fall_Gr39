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
    boolean existsByClassEntityClassId(String classId);

    List<SessionEntity> findByClassEntityClassId(String classId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Session WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSessionBySessionId(@Param("sessionId") String sessionId);
}
