package com.nsg.repository;

import com.nsg.entity.AttendanceEntity;
import com.nsg.entity.ClassEntity;
import com.nsg.entity.CurriculumnEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumnRepository extends BaseRepository<CurriculumnEntity, String> {
//    void saveAll(List<CurriculumnEntity> curriculumnEntityList);
    Page<CurriculumnEntity> findByCurriculumnListEntityCurriculumnListId(int curriculumnListId, PageRequest of);

    List<CurriculumnEntity> findByCurriculumnListEntityCurriculumnListId(int curriculumnListId);
}



