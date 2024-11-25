package com.nsg.service;

import com.nsg.dto.request.curriculumn.CurriculumnListRequest;
import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.entity.CurriculumnEntity;
import com.nsg.entity.CurriculumnListEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurriculumnListService {

    //create
    void createCurriculumnList(CurriculumnListRequest request);

    //get all curriculumn
    List<CurriculumnListResponse> getAllCurriculumnList();

    //get a curriculumn list by id
    CurriculumnListResponse getCurriculumnList(int id);

    //update
    CurriculumnListResponse updateCurriculumnList(int id, CurriculumnListRequest request);

    //delete
    void deleteCurriculumnList(int id);

    CurriculumnListResponse toCurriculumnListResponse(CurriculumnListEntity curriculumnListEntity);

}
