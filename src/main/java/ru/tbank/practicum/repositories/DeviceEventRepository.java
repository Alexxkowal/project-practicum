package ru.tbank.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.DeviceEvent;

public interface DeviceEventRepository extends JpaRepository<DeviceEvent, Long> {}
