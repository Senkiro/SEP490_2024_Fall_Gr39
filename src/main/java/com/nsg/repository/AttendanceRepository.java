package com.nsg.repository;

import com.nsg.dto.response.attendance.AttendanceStatisticsResponse;
import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends BaseRepository<AttendanceEntity, String> {
    Page<AttendanceEntity> findBySessionEntitySessionId(String sessionId, PageRequest of);

    Page<AttendanceEntity> findByStudentEntityStudentId(String studentId, PageRequest of);

    @Query("SELECT a FROM AttendanceEntity a " +
            "JOIN a.studentEntity s " +
            "JOIN s.batchEntity b " +
            "WHERE b.batchStatus = :batchStatus")
    List<AttendanceEntity> findByBatchStatus(@Param("batchStatus") int batchStatus);

    void deleteBySessionEntity(SessionEntity session);

    @Query("SELECT DISTINCT e FROM AttendanceEntity a JOIN a.sessionEntity.curriculumnEntity.examEntity e WHERE a.studentEntity.studentId = :studentId")
    Page<ExamEntity> findExamsByStudentId(@Param("studentId") String studentId, Pageable pageable);

    // calculate attend number of one student

    @Query(value = "SELECT " +
            "   COUNT(CASE WHEN status = 'Attended' THEN 1 END) AS attendCount, " +
            "   COUNT(*) AS totalCount, " +
            "   (COUNT(CASE WHEN status = 'Attended' THEN 1 END) * 100.0 / COUNT(*)) AS attendPercentage " +
            "FROM attendance " +
            "WHERE student_id = :studentId",
            nativeQuery = true)
    List<Object[]> getAttendanceStatistics(@Param("studentId") String studentId);

    @Query("SELECT COUNT(a) > 0 FROM AttendanceEntity a WHERE a.studentEntity.studentId = :studentId")
    boolean existsByStudentId(@Param("studentId") String studentId);

    @Query(value = """
        SELECT COUNT(*) AS total_attendance
        FROM attendance a
        JOIN session s ON a.session_id = s.session_id
        WHERE a.status = 'Attend'
          AND s.date = :date
    """, nativeQuery = true)
    int countAttendanceByDate(@Param("date") LocalDate date);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM attendance WHERE session_id IN (" +
            "  SELECT session_id FROM session WHERE class_id = :classId" +
            ")", nativeQuery = true)
    void deleteByClassId(@Param("classId") String classId);

}
