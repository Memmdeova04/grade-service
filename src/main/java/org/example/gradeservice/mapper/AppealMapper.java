package org.example.gradeservice.mapper;


import org.example.gradeservice.dto.AppealMessageResponse;
import org.example.gradeservice.dto.AppealResponse;
import org.example.gradeservice.entity.Appeal;
import org.example.gradeservice.entity.AppealMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppealMapper {

    AppealResponse toResponse(Appeal appeal);

    @Mapping(target = "appealId", source = "appeal.id")
    @Mapping(target = "senderRole", source = "senderRole")
    AppealMessageResponse toMessageResponse(AppealMessage message);
}