package com.nsg.repository;

import com.nsg.entity.EventFeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventFeedbackRepository extends JpaRepository<EventFeedbackEntity, String> {
    Page<EventFeedbackEntity> findByEventEntityEventId(String eventId, PageRequest pageable);

    Page<EventFeedbackEntity> findByStudentEntityStudentId(String studentId, PageRequest pageable);

    List<EventFeedbackEntity> findByEventEntityEventId(String eventId);

}
