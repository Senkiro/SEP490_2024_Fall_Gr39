package com.nsg.repository;

import com.nsg.entity.ExamEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends BaseRepository<ExamEntity, String> {

    Optional<ExamEntity> findByExamTitle(String examTitle);

    @Query(value = "SELECT DISTINCT e.* " +
            "FROM exam e " +
            "JOIN curriculumn c ON e.exam_id = c.exam_id " +
            "JOIN session s ON c.curriculumn_id = s.curriculumn_id " +
            "WHERE s.class_id = :classId AND c.exam_id IS NOT NULL", nativeQuery = true)
    List<ExamEntity> findExamsByClassId(@Param("classId") String classId);

}
