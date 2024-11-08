package com.nsg.repository;

import com.nsg.entity.TimeSlotEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends BaseRepository<TimeSlotEntity, String> {
    @Query("SELECT t FROM TimeSlotEntity t WHERE t.startTime = :startTime AND t.endTime = :endTime")
    Optional<TimeSlotEntity> findByStartTimeAndEndTime(@Param("startTime") LocalTime startTime,
                                                       @Param("endTime") LocalTime endTime);
}
