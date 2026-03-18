package ru.tbank.practicum.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.models.Heater;

@Repository
public class StubHeaterRepository implements HeaterRepostory {
    private final Map<Long, Heater> heaters = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public StubHeaterRepository() {
        save(new Heater(null, 18.5, false, 22.0));
        save(new Heater(null, 21.0, true, 23.5));
    }

    public Heater save(Heater heater) {
        if (heater.getId() == null) {
            heater.setId(idCounter.getAndIncrement());
        }
        heaters.put(heater.getId(), heater);
        return heater;
    }

    public Optional<Heater> findById(Long id) {
        return Optional.ofNullable(heaters.get(id));
    }

    public List<Heater> findAll() {
        return new ArrayList<>(heaters.values());
    }
}
