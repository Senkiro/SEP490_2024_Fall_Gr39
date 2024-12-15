package com.nsg.repository;

import com.nsg.entity.ClassEntity;
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
public interface ClassRepository extends BaseRepository<ClassEntity, String> {
    //ClassEntity findByClassName(String className);

    Page<ClassEntity> findAll(Pageable pageable);

    Page<ClassEntity> findByBatchEntityBatchName(String batchName, PageRequest of);

    List<ClassEntity> findByClassNameAndBatchEntityBatchName(String className, String batchName);

    List<ClassEntity> findByClassName(String className);

    ClassEntity findByClassId(String classId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM class WHERE class_id = :classId", nativeQuery = true)
    void deleteByClassId(@Param("classId") String classId);
}



