package ru.tbank.practicum.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.Heater;

public interface HeaterRepository extends JpaRepository<Heater, Long> {

    @EntityGraph(attributePaths = "device")
    @Override
    Optional<Heater> findById(Long id);

    @EntityGraph(attributePaths = "device")
    @Override
    List<Heater> findAll();
}
