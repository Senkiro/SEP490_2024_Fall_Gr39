package com.nsg.Mapper;
import com.nsg.dto.request.batch.BatchCreationRequest;
import com.nsg.entity.BatchEntity;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface TeacherMapper {
    BatchEntity toBatchEntity(BatchCreationRequest batchCreationRequest);
}