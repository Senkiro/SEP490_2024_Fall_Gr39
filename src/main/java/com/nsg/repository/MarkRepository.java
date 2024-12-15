package com.nsg.repository;

import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.MarkEntity;
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
public interface MarkRepository extends BaseRepository<MarkEntity, String> {
    List<MarkEntity> findByStudentEntityStudentId(String studentId);
    boolean existsByStudentEntityStudentIdAndExamEntityExamId(String studentId, int examId);

    List<MarkEntity> findByExamEntityExamId(int examId);

    @Query(value = "SELECT m.* FROM mark m " +
            "JOIN student s ON m.student_id = s.user_id " +
            "WHERE m.exam_id = :examId " +
            "AND s.class_id = :classId", nativeQuery = true)
    List<MarkEntity> findMarksByExamIdAndClassId(@Param("examId") Long examId,
                                                 @Param("classId") String classId);

    boolean existsByStudentEntityStudentId(String studentId);

    //count marked in one day
    @Query(value = """
    SELECT COUNT(m.mark_id) AS total_marked
    FROM mark m
    JOIN curriculumn c ON m.exam_id = c.exam_id
    JOIN session s ON c.curriculumn_id = s.curriculumn_id
    WHERE m.status = 1
      AND s.date = :date
      AND s.curriculumn_id IS NOT NULL
      AND s.class_id = :classId
    """, nativeQuery = true)
    int countMarkedByStatusAndClass(@Param("date") LocalDate date,
                                    @Param("classId") String classId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM mark WHERE student_id IN (" +
            "  SELECT user_id FROM student WHERE class_id = :classId" +
            ")", nativeQuery = true)
    void deleteMarksByClassId(@Param("classId") String classId);

}
