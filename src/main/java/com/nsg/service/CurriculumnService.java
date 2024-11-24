package com.nsg.service;

import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.entity.CurriculumnEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurriculumnService {
    void createCurriculumn(List<CurriculumnEntity> curriculumnEntityList);

    //get all
    List<CurriculumnResponse> getAllCurriculumn();
}
