package com.nsg.repository;

import com.nsg.entity.HolidayEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends BaseRepository<HolidayEntity, String> {
    Page<HolidayEntity> findAll(Pageable pageable);

    List<HolidayEntity> findAll();

    Optional<HolidayEntity> findByHolidayId(String holidayId);
}
