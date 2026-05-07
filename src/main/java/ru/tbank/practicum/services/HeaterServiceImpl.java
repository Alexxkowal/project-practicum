package ru.tbank.practicum.services;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.exception.HeaterNotFoundException;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.models.Device;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.repositories.DeviceRepository;
import ru.tbank.practicum.repositories.HeaterRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeaterServiceImpl implements HeaterService {
    private final HeaterRepository heaterRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public Heater save(Heater heater) {
        return heaterRepository.save(heater);
    }

    @Override
    @Transactional
    public Heater updateTargetTemperature(Long id, Double temperature) {
        Heater heater = getHeaterById(id);
        disableAutoMode(heater.getDevice());
        heater.setTargetTemp(temperature);
        return heaterRepository.save(heater);
    }

    @Override
    @Transactional
    public Heater updateStatus(Long id, Boolean isWorking) {
        if (isWorking == null) {
            throw new IllegalArgumentException("Поле isWorking обязательно");
        }
        Heater heater = getHeaterById(id);
        disableAutoMode(heater.getDevice());
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
                    if (command.value() != null) {
                        heater.setTargetTemp(command.value());
                    }
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
                if (command.value() != null && !Objects.equals(heater.getTargetTemp(), command.value())) {
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

    private void disableAutoMode(Device device) {
        if (device != null && device.isAutoMode()) {
            device.setAutoMode(false);
            deviceRepository.save(device);
        }
    }
}
