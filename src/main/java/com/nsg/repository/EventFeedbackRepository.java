package com.nsg.repository;

import com.nsg.entity.EventEntity;
import com.nsg.entity.EventFeedbackEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventFeedbackRepository extends JpaRepository<EventFeedbackEntity, String> {
    Page<EventFeedbackEntity> findByEventEntityEventId(String eventId, PageRequest pageable);

    Page<EventFeedbackEntity> findByStudentEntityStudentId(String studentId, PageRequest pageable);

    @Query(value = "SELECT * FROM Event " +
            "WHERE class_id = :classId " +
            "AND attendance_status is not null " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<EventEntity> findSessionsAttendanceStatus(@Param("classId") String classId);
}
