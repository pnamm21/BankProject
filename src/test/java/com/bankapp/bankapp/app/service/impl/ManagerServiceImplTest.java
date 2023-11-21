package com.bankapp.bankapp.app.service.impl;

import com.bankapp.bankapp.app.dto.ManagerDtoFullUpdate;
import com.bankapp.bankapp.app.dto.ProductDtoFullUpdate;
import com.bankapp.bankapp.app.entity.Manager;
import com.bankapp.bankapp.app.entity.Product;
import com.bankapp.bankapp.app.entity.enums.CurrencyCodeType;
import com.bankapp.bankapp.app.entity.enums.ManagerStatus;
import com.bankapp.bankapp.app.entity.enums.ProductStatus;
import com.bankapp.bankapp.app.exception.DataNotFoundException;
import com.bankapp.bankapp.app.mapper.ManagerMapper;
import com.bankapp.bankapp.app.repository.ManagerRepository;
import com.bankapp.bankapp.app.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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

    private static final UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getManagerByIdTest() {

        Manager manager = new Manager();
        manager.setId(uuid);

        when(managerRepository.findById(uuid)).thenReturn(Optional.of(manager));

        Optional<Manager> result = managerService.getManagerById(uuid.toString());

        assertTrue(result.isPresent());
        assertEquals(uuid, result.get().getId());

        verify(managerRepository, times(1)).findById(uuid);
    }

    @Test
    void getManagerByIdNotFoundTest() {

        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            managerService.getManagerById(uuid.toString());
        });

        verify(managerRepository, times(1)).findById(uuid);
    }

    @Test
    void updateManagerTest() {

        ManagerDtoFullUpdate managerDtoFullUpdate = new ManagerDtoFullUpdate();
        managerDtoFullUpdate.setFirstName("Name");
        managerDtoFullUpdate.setLastName("Name");
        managerDtoFullUpdate.setStatus("ACTIVE");

        Manager originalManager = new Manager();
        originalManager.setId(uuid);

        Manager updatedManager = new Manager();
        updatedManager.setId(uuid);
        updatedManager.setFirstName(managerDtoFullUpdate.getFirstName());
        updatedManager.setLastName(managerDtoFullUpdate.getLastName());
        updatedManager.setStatus(ManagerStatus.valueOf(managerDtoFullUpdate.getStatus()));

        when(managerRepository.existsById(uuid)).thenReturn(true);
        when(managerRepository.findById(uuid)).thenReturn(Optional.of(originalManager));
        when(managerMapper.managerDtoFullToManager(managerDtoFullUpdate)).thenReturn(updatedManager);
        when(managerMapper.mergeManagers(updatedManager, originalManager)).thenReturn(updatedManager);
        when(managerRepository.save(updatedManager)).thenReturn(updatedManager);

        Manager result = managerService.updateManager(uuid.toString(), managerDtoFullUpdate);

        assertNotNull(result);
        assertEquals(uuid, result.getId());
        assertEquals(managerDtoFullUpdate.getFirstName(), result.getFirstName());
        assertEquals(managerDtoFullUpdate.getLastName(), result.getLastName());
        assertEquals(managerDtoFullUpdate.getStatus(), result.getStatus().toString());

        verify(managerRepository, times(1)).existsById(uuid);
        verify(managerRepository, times(1)).findById(uuid);
        verify(managerMapper, times(1)).managerDtoFullToManager(managerDtoFullUpdate);
        verify(managerMapper, times(1)).mergeManagers(updatedManager, originalManager);
        verify(managerRepository, times(1)).save(updatedManager);
    }

    @Test
    void updateManagerNotFoundTest() {

        ManagerDtoFullUpdate managerDtoFullUpdate = new ManagerDtoFullUpdate();

        when(managerRepository.existsById(uuid)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> {
            managerService.updateManager(uuid.toString(), managerDtoFullUpdate);
        });

        verify(managerRepository, times(1)).existsById(uuid);
        verify(managerRepository, never()).findById(uuid);
        verify(managerMapper, never()).managerDtoFullToManager(managerDtoFullUpdate);
        verify(managerMapper, never()).mergeManagers(any(), any());
        verify(managerRepository, never()).save(any());
    }

}