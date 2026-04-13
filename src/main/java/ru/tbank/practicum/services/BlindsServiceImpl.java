package ru.tbank.practicum.services;

import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.exception.BlindsNotFoundException;
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

    private Blinds getBlindsById(Long id) {
        return blindsRepository.findById(id).orElseThrow(() -> new BlindsNotFoundException(id));
    }
}
