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

/**
 * Agreement Mapper
 * @author Fam Le Duc Nam
 */
@Mapper(componentModel = "spring", uses = UUID.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgreementMapper {
    AgreementDto agreementToAgreementDto(Agreement agreement);

    @Mapping(target="accountId",source = "account.id")
    @Mapping(target="productId",source = "product.id")
    AgreementFullDtoUpdate agreementToAgreementFullDto(Agreement agreement);
    Agreement agreementFullDtoToAgreement(AgreementFullDtoUpdate agreementFullDto);

    Agreement mergeAgreement(Agreement from, @MappingTarget Agreement to);
    List<AgreementDto> listAgreementToListAgreementDto(List<Agreement> agreements);

}