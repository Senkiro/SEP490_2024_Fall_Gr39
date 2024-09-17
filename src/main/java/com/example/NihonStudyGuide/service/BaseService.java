package com.example.NihonStudyGuide.service;
import org.springframework.transaction.annotation.Transactional;

public interface BaseService<T, ID> {
    @Transactional
    Object create(Object request);

    @Transactional
    Object update(ID id, Object request);

    @Transactional
    Object get(ID id);

    @Transactional
    void delete(ID id);

    Object search(Object request);

}
