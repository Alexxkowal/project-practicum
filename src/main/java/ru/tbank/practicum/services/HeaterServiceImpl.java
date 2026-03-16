package ru.tbank.practicum.services;

import org.springframework.stereotype.Service;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.HeaterRepository;

@Service
public class HeaterServiceImpl implements HeaterService {
    private final HeaterRepository heaterRepository;

    public HeaterServiceImpl(HeaterRepository heaterRepository) {
        this.heaterRepository = heaterRepository;
    }

    @Override
    public Heater save(Heater heater) {
        return heaterRepository.save(heater);
    }

    @Override
    public Heater updateTargetTemperature(Long id, Double temperature){
        Heater heater = getHeaterById(id);
        heater.setTargetTemperature(temperature);
        return heaterRepository.save(heater);
    }

    @Override
    public Heater updateStatus(Long id, Boolean isWorking){
        Heater heater = getHeaterById(id);
        heater.setWorking(isWorking);
        return heaterRepository.save(heater);
    }

    private Heater getHeaterById(Long id){
        return heaterRepository.findById(id).orElseThrow(() -> new RuntimeException("Батарея не найдена"));
    }
}
