package com.nsg.repository;

import com.nsg.entity.ExamTypeRateEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamTypeRepository extends BaseRepository<ExamTypeRateEntity, String> {
    Optional<ExamTypeRateEntity> findByExamType(int examType);
}
