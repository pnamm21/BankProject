package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ManagerDto;
import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.entity.enums.ManagerStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.ManagerMapper;
import com.bankapp.bankapp.app.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ManagerMapper managerMapper;
    @InjectMocks
    private ManagerServiceImpl managerService;

    private static final UUID managerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getManagerByIdTest() {

        Manager manager = new Manager();
        ManagerDto expectedManagerDto = new ManagerDto();

        when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(managerMapper.managerToManagerDto(manager)).thenReturn(expectedManagerDto);

        ManagerDto result = managerService.getManagerById(managerId.toString());

        assertNotNull(result);

        verify(managerRepository, times(1)).findById(managerId);
        verify(managerMapper, times(1)).managerToManagerDto(manager);
    }

    @Test
    void getManagerByIdNotFoundTest() {

        when(managerRepository.findById(managerId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            managerService.getManagerById(managerId.toString());
        });

        verify(managerRepository, times(1)).findById(managerId);
    }

    @Test
    void updateManagerTest() {

        ManagerDtoFullUpdate managerDtoFullUpdate = new ManagerDtoFullUpdate();
        Manager originalManager = new Manager();
        Manager updatedManager = new Manager();
        ManagerDto expectedManagerDto = new ManagerDto();

        when(managerRepository.existsById(managerId)).thenReturn(true);
        when(managerRepository.findById(managerId)).thenReturn(Optional.of(originalManager));
        when(managerMapper.managerDtoFullToManager(managerDtoFullUpdate)).thenReturn(updatedManager);
        when(managerMapper.mergeManagers(updatedManager, originalManager)).thenReturn(updatedManager);
        when(managerMapper.managerToManagerDto(updatedManager)).thenReturn(expectedManagerDto);

        ManagerDto result = managerService.updateManager(managerId.toString(), managerDtoFullUpdate);

        assertNotNull(result);

        verify(managerRepository, times(1)).existsById(managerId);
        verify(managerRepository, times(1)).findById(managerId);
        verify(managerRepository, times(1)).save(updatedManager);
        verify(managerMapper, times(1)).managerDtoFullToManager(managerDtoFullUpdate);
        verify(managerMapper, times(1)).mergeManagers(updatedManager, originalManager);
        verify(managerMapper, times(1)).managerToManagerDto(updatedManager);

    }

    @Test
    void updateManagerNotFoundTest() {

        ManagerDtoFullUpdate managerDtoFullUpdate = new ManagerDtoFullUpdate();

        when(managerRepository.existsById(managerId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> {
            managerService.updateManager(managerId.toString(), managerDtoFullUpdate);
        });

        verify(managerRepository, times(1)).existsById(managerId);
        verify(managerRepository, never()).findById(managerId);
        verify(managerMapper, never()).managerDtoFullToManager(managerDtoFullUpdate);
        verify(managerMapper, never()).mergeManagers(any(), any());
        verify(managerRepository, never()).save(any());
    }

}