package com.nsg.repository;

import com.nsg.entity.ExamEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends BaseRepository<ExamEntity, String> {

    Optional<ExamEntity> findByExamTitle(String examTitle);

}
