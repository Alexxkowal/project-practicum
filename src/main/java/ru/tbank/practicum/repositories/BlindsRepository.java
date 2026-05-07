package ru.tbank.practicum.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.Blinds;

public interface BlindsRepository extends JpaRepository<Blinds, Long> {

    @EntityGraph(attributePaths = "device")
    @Override
    Optional<Blinds> findById(Long id);

    @EntityGraph(attributePaths = "device")
    @Override
    List<Blinds> findAll();
}
