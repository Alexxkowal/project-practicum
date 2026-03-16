package ru.tbank.practicum.services;

import org.springframework.stereotype.Service;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.HeaterRepostory;
import ru.tbank.practicum.repositories.StubHeaterRepository;

@Service
public class HeaterServiceImpl implements HeaterService {
    private final HeaterRepostory heaterRepostory;

    public HeaterServiceImpl(HeaterRepostory heaterRepostory) {
        this.heaterRepostory = heaterRepostory;
    }

    @Override
    public Heater save(Heater heater) {
        return heaterRepostory.save(heater);
    }

    @Override
    public Heater updateTargetTemperature(Long id, Double temperature){
        Heater heater = getHeaterById(id);
        heater.setTargetTemperature(temperature);
        return heaterRepostory.save(heater);
    }

    @Override
    public Heater updateStatus(Long id, Boolean isWorking){
        Heater heater = getHeaterById(id);
        heater.setWorking(isWorking);
        return heaterRepostory.save(heater);
    }

    private Heater getHeaterById(Long id){
        return heaterRepostory.findById(id).orElseThrow(() -> new RuntimeException("Батарея не найдена"));
    }
}
