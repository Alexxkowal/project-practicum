package ru.tbank.practicum.repositories;

import java.util.List;
import java.util.Optional;
import ru.tbank.practicum.models.Blinds;

public interface BlindsRepository {
    Blinds save(Blinds blinds);

    Optional<Blinds> findById(Long id);

    List<Blinds> findAll();
}
