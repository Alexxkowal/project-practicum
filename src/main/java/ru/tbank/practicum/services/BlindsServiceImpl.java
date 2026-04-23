package ru.tbank.practicum.services;

import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.exception.BlindsNotFoundException;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.repositories.BlindsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlindsServiceImpl implements BlindsService {
    private final BlindsRepository blindsRepository;

    @Override
    public Blinds updatePosition(Long id, Integer position) {
        Blinds blinds = getBlindsById(id);
        blinds.setTargetPosition(position);
        return blindsRepository.save(blinds);
    }

    @Override
    public Blinds updateSchedule(Long id, LocalTime openTime, LocalTime closeTime) {
        Blinds blinds = getBlindsById(id);
        if (openTime != null) {
            blinds.setOpenTime(openTime);
        }
        if (closeTime != null) {
            blinds.setCloseTime(closeTime);
        }
        return blindsRepository.save(blinds);
    }

    @Override
    @Transactional
    public void processCommand(DeviceCommand command) {
        Blinds blinds = getBlindsById(command.deviceId());
        switch (command.action()) {
            case SET_VALUE -> {
                int newValue = command.value().intValue();
                if (blinds.getTargetPosition() != newValue) {
                    blinds.setTargetPosition(newValue);
                    log.info("Шторы {}: целевая позиция изменена на {}%", blinds.getId(), newValue);
                }
            }
            case TURN_OFF -> {
                if (blinds.getTargetPosition() != 0) {
                    blinds.setTargetPosition(0);
                }
            }
            default -> log.warn("Действие {} не поддерживается для штор", command.action());
        }
    }

    @Override
    public List<Blinds> getAllBlinds() {
        return blindsRepository.findAll();
    }

    private Blinds getBlindsById(Long id) {
        return blindsRepository.findById(id).orElseThrow(() -> new BlindsNotFoundException(id));
    }
}
