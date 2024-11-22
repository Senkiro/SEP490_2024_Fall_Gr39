package com.nsg.repository;

import com.nsg.entity.LessonEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends BaseRepository<LessonEntity, String> {

}
