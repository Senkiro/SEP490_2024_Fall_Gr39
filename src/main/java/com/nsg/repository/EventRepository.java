package com.nsg.repository;

import com.nsg.entity.BatchEntity;
import com.nsg.entity.EventEntity;
import com.nsg.entity.LessonEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<EventEntity, String> {
    Optional<EventEntity> findByEventId(String eventId);

    List<EventEntity> findByEventName(String eventName);

    List<EventEntity> findByEventNameContainingIgnoreCase(String eventName);

}
