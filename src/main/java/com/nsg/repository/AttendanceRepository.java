package com.nsg.repository;

import com.nsg.dto.response.attendance.AttendanceStatisticsResponse;
import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends BaseRepository<AttendanceEntity, String> {
    Page<AttendanceEntity> findBySessionEntitySessionId(String sessionId, PageRequest of);

    Page<AttendanceEntity> findByStudentEntityStudentId(String studentId, PageRequest of);

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

//    @Query("SELECT " +
//            "   COUNT(CASE WHEN a.status = 'attend' THEN 1 ELSE NULL END), " +
//            "   COUNT(a), " +
//            "   (COUNT(CASE WHEN a.status = 'attend' THEN 1 ELSE NULL END) * 1.0 / COUNT(a) * 100) " +
//            "FROM AttendanceEntity a " +
//            "WHERE a.studentId = :studentId")
//    List<Object[]> getAttendanceStatisticsRaw(@Param("studentId") String studentId);

}
