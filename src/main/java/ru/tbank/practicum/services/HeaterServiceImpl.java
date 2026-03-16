package ru.tbank.practicum.services;

import org.springframework.stereotype.Service;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.HeaterRepository;

@Service
public class HeaterInterfaceImpl implements HeaterService {
    private final HeaterRepository heaterRepository;

    public HeaterInterfaceImpl(HeaterRepository heaterRepository) {
        this.heaterRepository = heaterRepository;
    }

    @Override
    public Heater save(Heater heater) {
        return heaterRepository.save(heater);
    }

    public Heater update(Long id, HeaterRequestDTO dto) {
        Heater heater = heaterRepository.findById(id).orElseThrow(() -> new RuntimeException("Батарея не найдена"));
        if (dto.getTargetTemperature() != null) {
            heater.setTargetTemperature(dto.getTargetTemperature());
        }
        if (dto.getIsWorking() != null) {
            heater.setWorking(dto.getIsWorking());
        }
        return heaterRepository.save(heater);
    }
}
