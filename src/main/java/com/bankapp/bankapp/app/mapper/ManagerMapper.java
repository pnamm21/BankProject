package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ManagerMapper {
    ManagerDto managerToManagerDto(Manager manager);
    Manager managerDtoToManager(ManagerDto managerDto);
    ManagerDtoFullUpdate ManagerToManagerDtoFUllUpdate(Manager manager);

    Manager mergeManagers(Manager from, @MappingTarget Manager to);

    Manager managerDtoFullToManager(ManagerDtoFullUpdate managerDtoFullUpdate);


}