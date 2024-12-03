package com.nsg.repository;

import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.ExamEntity;
import com.nsg.entity.MarkEntity;
import com.nsg.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
