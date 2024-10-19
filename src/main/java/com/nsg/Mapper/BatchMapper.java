package com.nsg.Mapper;

import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.Batch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BatchMapper {
    Batch toBatch(BatchCreationRequest batchCreationRequest);
}
