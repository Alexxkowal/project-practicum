package ru.tbank.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {}
