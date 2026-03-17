package ru.tbank.practicum.repositories;

import java.util.List;
import java.util.Optional;
import ru.tbank.practicum.models.Heater;

public interface HeaterRepostory {
    public Heater save(Heater heater);

    Optional<Heater> findById(Long id);

    List<Heater> findAll();
}
