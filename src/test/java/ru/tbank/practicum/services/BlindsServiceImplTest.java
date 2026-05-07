package ru.tbank.practicum.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.exception.BlindsNotFoundException;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.models.Device;
import ru.tbank.practicum.repositories.BlindsRepository;
import ru.tbank.practicum.repositories.DeviceRepository;

@ExtendWith(MockitoExtension.class)
class BlindsServiceImplTest {

    @Mock
    private BlindsRepository blindsRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private BlindsServiceImpl blindsService;

    @Test
    public void updatePosition_ShouldReturnUpdatedBlinds() {
        Long id = 1L;
        Integer newPosition = 50;
        Blinds blinds = new Blinds();
        blinds.setId(id);
        blinds.setTargetPosition(0);

        when(blindsRepository.findById(id)).thenReturn(Optional.of(blinds));
        when(blindsRepository.save(any(Blinds.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Blinds result = blindsService.updatePosition(id, newPosition);

        assertNotNull(result);
        assertEquals(newPosition, result.getTargetPosition());
        verify(blindsRepository).save(blinds);
    }

    @Test
    public void updatePosition_ShouldThrowException_WhenNotFound() {
        Long id = 99L;
        when(blindsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(BlindsNotFoundException.class, () -> {
            blindsService.updatePosition(id, 100);
        });
        verify(blindsRepository, never()).save(any());
    }

    @Test
    public void updatePosition_ShouldDisableAutoMode_WhenDeviceInAuto() {
        Long id = 1L;
        Device device = new Device();
        device.setAutoMode(true);
        Blinds blinds = new Blinds();
        blinds.setId(id);
        blinds.setDevice(device);
        blinds.setTargetPosition(0);
        when(blindsRepository.findById(id)).thenReturn(Optional.of(blinds));
        when(blindsRepository.save(any(Blinds.class))).thenAnswer(invocation -> invocation.getArgument(0));

        blindsService.updatePosition(id, 50);

        assertFalse(device.isAutoMode());
        verify(deviceRepository).save(device);
    }
}
