package ru.tbank.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.HeaterNotFoundException;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.HeaterRepository;

@Service
@RequiredArgsConstructor
public class HeaterServiceImpl implements HeaterService {
    private final HeaterRepository heaterRepository;

    @Override
    public Heater save(Heater heater) {
        return heaterRepository.save(heater);
    }

    @Override
    public Heater updateTargetTemperature(Long id, Double temperature) {
        Heater heater = getHeaterById(id);
        heater.setTargetTemp(temperature);
        return heaterRepository.save(heater);
    }

    @Override
    public Heater updateStatus(Long id, Boolean isWorking) {
        Heater heater = getHeaterById(id);
        heater.setWorking(isWorking);
        return heaterRepository.save(heater);
    }

    private Heater getHeaterById(Long id) {
        return heaterRepository.findById(id).orElseThrow(() -> new HeaterNotFoundException(id));
    }
}
