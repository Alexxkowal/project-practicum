package ru.tbank.practicum.repositories;

import ru.tbank.practicum.models.Heater;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface HeaterRepostory {
    public Heater save(Heater heater);

    Optional<Heater> findById(Long id);

    List<Heater> findAll();
}
