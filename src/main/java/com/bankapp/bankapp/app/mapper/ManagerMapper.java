package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

/**
 * Manager Mapper
 * @author Fam Le Duc Nam
 */
@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ManagerMapper {
    ManagerDto managerToManagerDto(Manager manager);
    Manager managerDtoToManager(ManagerDto managerDto);
    ManagerDtoFullUpdate ManagerToManagerDtoFUllUpdate(Manager manager);

    Manager mergeManagers(Manager from, @MappingTarget Manager to);

    Manager managerDtoFullToManager(ManagerDtoFullUpdate managerDtoFullUpdate);


}