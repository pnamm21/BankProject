package com.bankapp.bankapp.app.mapper;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.entity.Manager;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ManagerMapper {
    ManagerDto managerToManagerDto(Manager manager);
    Manager managerDtoToManager(ManagerDto managerDto);
}
