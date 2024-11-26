package com.nsg.repository;

import com.nsg.entity.EventEntity;
import com.nsg.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends BaseRepository<NewEntity, String> {
    Page<NewEntity> findAll(Pageable pageable);

    Page<NewEntity> findAllByStatusTrue(Pageable pageable);

    Optional<NewEntity> findByNewId(String eventId);
}
