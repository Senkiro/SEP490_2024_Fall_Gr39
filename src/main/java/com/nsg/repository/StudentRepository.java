package com.nsg.repository;

import com.nsg.dto.response.student.StudentResponse;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends BaseRepository<StudentEntity, String>{
    Page<StudentEntity> findByBatchEntityBatchName(String batchName, PageRequest pageable);

    Page<StudentEntity> findByBatchEntityBatchNameAndClassEntityClassName(String batchName, String className, PageRequest pageable);

    Page<StudentEntity> findByClassEntityClassId(String classEntityClassId, PageRequest pageable);
}
