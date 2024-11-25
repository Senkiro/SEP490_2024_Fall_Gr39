package com.nsg.repository;

import com.nsg.entity.HolidayEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends BaseRepository<HolidayEntity, String> {
    Page<HolidayEntity> findAll(Pageable pageable);
}
