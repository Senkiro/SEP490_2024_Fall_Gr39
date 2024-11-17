package com.nsg.repository;

import com.nsg.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<EventEntity, String> {
    Optional<EventEntity> findByEventId(String eventId);

    List<EventEntity> findByEventName(String eventName);

    Page<EventEntity> findByEventNameContaining(String name, PageRequest of);
}

