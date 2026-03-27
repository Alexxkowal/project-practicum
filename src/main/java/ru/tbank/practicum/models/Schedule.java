package ru.tbank.practicum.models;

import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbank.practicum.models.enums.SchedulesAction;

@Entity
@Table(name = "schedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "execution_time", nullable = false)
    private LocalTime executionTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchedulesAction action;

    @Column(name = "target_value")
    private Double targetValue;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
