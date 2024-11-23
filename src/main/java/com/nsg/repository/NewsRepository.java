package com.nsg.repository;

import com.nsg.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends BaseRepository<NewEntity, String> {
    Page<NewEntity> findAll(Pageable pageable);
}
