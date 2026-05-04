package ru.tbank.practicum.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.exception.HeaterNotFoundException;
import ru.tbank.practicum.models.Device;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.DeviceRepository;
import ru.tbank.practicum.repositories.HeaterRepository;

@ExtendWith(MockitoExtension.class)
class HeaterServiceImplTest {

    @Mock
    private HeaterRepository heaterRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private HeaterServiceImpl heaterService;

    @Test
    public void updateStatus_ShouldChangeIsWorking() {
        Long id = 1L;
        Heater heater = new Heater();
        heater.setId(id);
        heater.setWorking(false);
        when(heaterRepository.findById(id)).thenReturn(Optional.of(heater));
        when(heaterRepository.save(any(Heater.class))).thenAnswer(i -> i.getArgument(0));
        Heater result = heaterService.updateStatus(id, true);
        assertTrue(result.isWorking());
        verify(heaterRepository, times(1)).save(any());
    }

    @Test
    public void updateTargetTemperature_ShouldThrowException() {
        Long id = 5L;
        when(heaterRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(HeaterNotFoundException.class, () -> heaterService.updateTargetTemperature(id, 25.5));
    }

    @Test
    public void updateTargetTemperature_ShouldDisableAutoMode_WhenDeviceInAuto() {
        Long id = 1L;
        Device device = new Device();
        device.setAutoMode(true);
        Heater heater = new Heater();
        heater.setId(id);
        heater.setDevice(device);
        heater.setTargetTemp(20.0);
        when(heaterRepository.findById(id)).thenReturn(Optional.of(heater));
        when(heaterRepository.save(any(Heater.class))).thenAnswer(i -> i.getArgument(0));

        heaterService.updateTargetTemperature(id, 24.0);

        assertFalse(device.isAutoMode());
        verify(deviceRepository).save(device);
    }
}
