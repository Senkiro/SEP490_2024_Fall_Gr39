package com.nsg.repository;

import com.nsg.entity.StudentEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends BaseRepository<StudentEntity, String>{
}
