package com.nsg.repository;

import com.nsg.entity.ClassEntity;
import com.nsg.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends BaseRepository<ClassEntity, String> {
    ClassEntity findByClassName(String className);

    Page<ClassEntity> findAll(Pageable pageable);

    Page<ClassEntity> findByBatchEntityBatchName(String batchName, PageRequest of);

    List<ClassEntity> findByClassNameAndBatchEntityBatchName(String className, String batchName);

}



