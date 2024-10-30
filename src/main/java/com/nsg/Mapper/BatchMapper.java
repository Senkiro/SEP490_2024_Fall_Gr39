package com.nsg.Mapper;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.BatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BatchMapper {
    BatchEntity toBatchEntity(BatchCreationRequest batchCreationRequest);
}
