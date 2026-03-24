package ru.tbank.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {}
