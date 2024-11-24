package com.nsg.service;

import com.nsg.dto.request.curriculumn.CurriculumnListRequest;
import com.nsg.entity.CurriculumnEntity;
import com.nsg.entity.CurriculumnListEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurriculumnListService {

    //create
    void createCurriculumnList(CurriculumnListRequest request);

    //get a curriculumn list
    List<CurriculumnListEntity> getCurriculumnList();

}
