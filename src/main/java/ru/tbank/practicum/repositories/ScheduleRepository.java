package ru.tbank.practicum.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tbank.practicum.models.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @EntityGraph(attributePaths = "device")
    List<Schedule> findAllByDeviceIdOrderByExecutionTimeAsc(Long deviceId);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.device WHERE s.isActive = true")
    List<Schedule> findAllActiveWithDevice();
}
