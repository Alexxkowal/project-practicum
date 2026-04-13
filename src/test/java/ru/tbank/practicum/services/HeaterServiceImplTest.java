package ru.tbank.practicum.services;

import static org.junit.jupiter.api.Assertions.*;
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
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.HeaterRepository;

@ExtendWith(MockitoExtension.class)
class HeaterServiceImplTest {

    @Mock
    private HeaterRepository heaterRepository;

    @InjectMocks
    private HeaterServiceImpl heaterService;

    @Test
    void updateStatus_ShouldChangeIsWorking() {
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
    void updateTargetTemperature_ShouldThrowException() {
        Long id = 5L;
        when(heaterRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(HeaterNotFoundException.class, () -> heaterService.updateTargetTemperature(id, 25.5));
    }
}
