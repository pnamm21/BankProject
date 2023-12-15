package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Account;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.exception.ExceptionMessage;
import com.bankapp.bankapp.app.mapper.ManagerMapper;
import com.bankapp.bankapp.app.repository.ManagerRepository;
import com.bankapp.bankapp.app.service.ManagerService;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Manager Service
 * @author ffam5
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    /**
     * @param managerRepository Manager Repository
     * @param managerMapper Manager Mapper
     */
    public ManagerServiceImpl(ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    /**
     * Find Manager by ID
     * @param id ManagerID
     * @return ManagerDto or throw DataNotFoundException
     */
    @Override
    public ManagerDto getManagerById(String id) throws DataNotFoundException {
        return managerMapper.managerToManagerDto(managerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND)));
    }

    /**
     * Update Manager
     * @param id ManagerID
     * @param managerDtoFullUpdate ManagerDtoFullUpdate
     * @return ManagerDto or throw DataNotFoundException
     */
    @Override
    public ManagerDto updateManager(String id, ManagerDtoFullUpdate managerDtoFullUpdate) {

        UUID stringId = UUID.fromString(id);
        if (managerRepository.existsById(stringId)) {
            managerDtoFullUpdate.setId(id);
            managerDtoFullUpdate.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            Manager manager = managerMapper.managerDtoFullToManager(managerDtoFullUpdate);
            Manager original = managerRepository.findById(stringId)
                    .orElseThrow(()->new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND));
            Manager updated = managerMapper.mergeManagers(manager, original);
            managerRepository.save(updated);

            return managerMapper.managerToManagerDto(updated);
        } else {
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }


}