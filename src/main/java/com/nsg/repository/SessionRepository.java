package com.nsg.repository;

import com.nsg.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends BaseRepository<SessionEntity, String> {
    List<SessionEntity> findBySessionWeekAndClassEntityClassId(int week, String classId);
    boolean existsByClassEntityClassId(String classId);
}
