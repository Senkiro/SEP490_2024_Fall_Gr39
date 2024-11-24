package com.nsg.repository;

import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.LessonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends BaseRepository<AttendanceEntity, String> {
    Page<AttendanceEntity> findBySessionEntitySessionId(String sessionId, PageRequest of);
}