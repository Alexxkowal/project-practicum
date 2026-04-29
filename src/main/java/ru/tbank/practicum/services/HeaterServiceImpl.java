package ru.tbank.practicum.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.exception.HeaterNotFoundException;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.HeaterRepository;

@Slf4j
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

    @Override
    @Transactional
    public void processCommand(DeviceCommand command) {
        Heater heater = getHeaterById(command.deviceId());
        boolean changed = false;

        switch (command.action()) {
            case TURN_ON -> {
                if (!heater.isWorking()) {
                    heater.setWorking(true);
                    heater.setTargetTemp(command.value());
                    changed = true;
                }
            }
            case TURN_OFF -> {
                if (heater.isWorking()) {
                    heater.setWorking(false);
                    changed = true;
                }
            }
            case SET_VALUE -> {
                if (!heater.getTargetTemp().equals(command.value())) {
                    heater.setTargetTemp(command.value());
                    changed = true;
                }
            }
        }

        if (changed) {
            heaterRepository.save(heater);
            log.info(
                    "Обогреватель {} обновлен: working={}, temp={}",
                    heater.getId(),
                    heater.isWorking(),
                    heater.getTargetTemp());
        }
    }

    @Override
    public List<Heater> getAllHeaters() {
        return heaterRepository.findAll();
    }

    private Heater getHeaterById(Long id) {
        return heaterRepository.findById(id).orElseThrow(() -> new HeaterNotFoundException(id));
    }
}
