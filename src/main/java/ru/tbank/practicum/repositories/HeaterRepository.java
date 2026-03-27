package ru.tbank.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.Heater;

public interface HeaterRepository extends JpaRepository<Heater, Long> {}
