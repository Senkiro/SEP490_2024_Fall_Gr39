package com.nsg.repository;

import com.nsg.entity.LessonEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends BaseRepository<LessonEntity, String> {

}
