package com.nsg.repository;

import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentRepository extends BaseRepository<StudentEntity, String>{
    Page<StudentEntity> findByBatchEntityBatchName(String batchName, PageRequest pageable);

    Page<StudentEntity> findByBatchEntityBatchNameAndClassEntityClassName(String batchName, String className, PageRequest pageable);

    List<StudentEntity> findByClassEntityClassId(String classId);

    Page<StudentEntity> findByClassEntityClassId(String classEntityClassId, PageRequest pageable);

    Page<StudentEntity> findByBatchEntityBatchNameAndClassEntityClassId(String batchName, String classId, PageRequest of);

    @Query("SELECT s FROM StudentEntity s WHERE s.batchEntity.batchName = :batchName ORDER BY s.avgMark DESC")
    List<StudentEntity> findTop10ByBatchOrderByAvgMarkDesc(@Param("batchName") String batchName, Pageable pageable);

    //delete student
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM student WHERE class_id = :classId", nativeQuery = true)
    void deleteByClassId(@Param("classId") String classId);

}
