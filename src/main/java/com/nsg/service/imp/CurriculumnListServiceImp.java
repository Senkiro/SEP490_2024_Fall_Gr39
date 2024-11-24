package com.nsg.service.imp;

import com.nsg.dto.request.curriculumn.CurriculumnListRequest;

import com.nsg.entity.CurriculumnListEntity;
import com.nsg.repository.CurriculumnListRepository;
import com.nsg.repository.CurriculumnRepository;
import com.nsg.service.CurriculumnListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurriculumnListServiceImp implements CurriculumnListService {

    @Autowired
    CurriculumnListRepository curriculumnListRepository;

    @Override
    public void createCurriculumnList(CurriculumnListRequest request) {
        CurriculumnListEntity curriculumnListEntity = new CurriculumnListEntity();
        curriculumnListEntity.setCurriculumnTitle(request.getCurriculumnTitle());

        curriculumnListRepository.save(curriculumnListEntity);
    }

    @Override
    public List<CurriculumnListEntity> getCurriculumnList() {

        List<CurriculumnListEntity> curriculumnListEntityList = curriculumnListRepository.findAll();

        return curriculumnListEntityList;
    }
}
