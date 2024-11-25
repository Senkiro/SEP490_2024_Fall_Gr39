package com.nsg.service.imp;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.curriculumn.CurriculumnListRequest;

import com.nsg.dto.response.curriculumn.CurriculumnListResponse;
import com.nsg.entity.CurriculumnListEntity;
import com.nsg.repository.CurriculumnListRepository;
import com.nsg.repository.CurriculumnRepository;
import com.nsg.service.CurriculumnListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<CurriculumnListResponse> getAllCurriculumnList() {

        List<CurriculumnListEntity> curriculumnListEntityList = curriculumnListRepository.findAll();

        List<CurriculumnListResponse> responseList = new ArrayList<>();

        for (CurriculumnListEntity curriculumnList : curriculumnListEntityList) {
            CurriculumnListResponse response = toCurriculumnListResponse(curriculumnList);
            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public CurriculumnListResponse getCurriculumnList(int id) {

        CurriculumnListEntity curriculumnListEntity = curriculumnListRepository.findById(String.valueOf(id)).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND)
        );

        return toCurriculumnListResponse(curriculumnListEntity);
    }

    @Override
    public CurriculumnListResponse updateCurriculumnList(int id, CurriculumnListRequest request) {

        CurriculumnListEntity curriculumnListEntity = curriculumnListRepository.findById(String.valueOf(id)).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND)
        );
        curriculumnListEntity.setCurriculumnTitle(request.getCurriculumnTitle());
        curriculumnListRepository.save(curriculumnListEntity);

        return getCurriculumnList(id);
    }

    @Override
    public void deleteCurriculumnList(int id) {
        curriculumnListRepository.deleteById(String.valueOf(id));
    }

    @Override
    public CurriculumnListResponse toCurriculumnListResponse(CurriculumnListEntity curriculumnListEntity) {
        CurriculumnListResponse curriculumnListResponse = new CurriculumnListResponse();

        curriculumnListResponse.setCurriculumnListId(curriculumnListEntity.getCurriculumnListId());
        curriculumnListResponse.setCurriculumnTitle(curriculumnListEntity.getCurriculumnTitle());

        return curriculumnListResponse;
    }
}
