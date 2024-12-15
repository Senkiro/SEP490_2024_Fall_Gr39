package com.nsg.repository;

import com.nsg.dto.response.event.EventResponse;
import com.nsg.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<EventEntity, String> {
    Optional<EventEntity> findByEventId(String eventId);

    List<EventEntity> findByEventName(String eventName);

    Page<EventEntity> findByEventNameContaining(String name, PageRequest of);

    @Query(value = """
                    SELECT e.* 
                    FROM event e
                    LEFT JOIN session s 
                        ON e.event_id = s.event_id AND s.class_id = :classId
                    WHERE s.event_id IS NULL
                """, nativeQuery = true)
    List<EventEntity> findEventsWithoutSessionsByClassIdNative(@Param("classId") String classId);

}

