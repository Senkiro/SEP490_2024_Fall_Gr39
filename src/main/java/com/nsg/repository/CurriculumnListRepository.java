package com.nsg.repository;

import com.nsg.entity.ClassEntity;
import com.nsg.entity.CurriculumnListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculumnListRepository extends BaseRepository<CurriculumnListEntity, String> {

    boolean existsByCurriculumnTitle(String title);
    Optional<CurriculumnListEntity> findByCurriculumnTitle(String title);
}



