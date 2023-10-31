package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.AgreementDto;
import com.bankapp.bankapp.app.dto.AgreementFullDtoUpdate;
import com.bankapp.bankapp.app.entity.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = UUID.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgreementMapper {
    AgreementDto agreementToAgreementDto(Agreement agreement);

    Agreement agreementFullDtoToAgreement(AgreementFullDtoUpdate agreementFullDto);

    Agreement mergeAgreement(Agreement from, @MappingTarget Agreement to);
    List<AgreementDto> listAgreementToListAgreementDto(List<Agreement> agreements);

}