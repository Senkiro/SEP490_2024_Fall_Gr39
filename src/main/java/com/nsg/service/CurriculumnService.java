package com.nsg.service;

import com.nsg.dto.request.curriculumn.CurriculumnRequest;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.dto.response.curriculumn.CurriculumnResponse;
import com.nsg.entity.CurriculumnEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurriculumnService {
    //create new
    void createCurriculumn(List<CurriculumnEntity> curriculumnEntityList);

    //get all
    List<CurriculumnResponse> getAllCurriculumn();

    //get one by id
    CurriculumnResponse getCurriculumn(String curriculumnId);

    //update
    CurriculumnResponse updateCurriculumn(String curriculumnId, CurriculumnRequest request);

    //delete
    void deleteCurriculumn(String curriculumnId);

    CurriculumnResponse toCurriculumnResponse(CurriculumnEntity curriculumnEntity);
}
