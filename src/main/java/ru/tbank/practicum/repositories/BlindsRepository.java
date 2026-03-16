package ru.tbank.practicum.repositories;

import ru.tbank.practicum.models.Blinds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BlindsRepository {
    Blinds save(Blinds blinds);

    Optional<Blinds> findById(Long id);

    List<Blinds> findAll();
}