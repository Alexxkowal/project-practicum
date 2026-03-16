package ru.tbank.practicum.repositories;


import org.springframework.stereotype.Repository;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.models.Heater;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BlindsRepository {
    private final Map<Long, Blinds> blindsMap = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public BlindsRepository() {
        save(new Blinds(null, 10, LocalTime.of(9, 0), LocalTime.of(21, 0)));
        save(new Blinds(null, 50, LocalTime.of(11, 30), LocalTime.of(19, 0)));
    }

    public Blinds save(Blinds blinds) {
        if (blinds.getId() == null) {
            blinds.setId(idCounter.getAndIncrement());
        }
        blindsMap.put(blinds.getId(), blinds);
        return blinds;
    }

    public Optional<Blinds> findById(Long id) {
        return Optional.ofNullable(blindsMap.get(id));
    }

    public List<Blinds> findAll() {
        return new ArrayList<>(blindsMap.values());
    }
}
