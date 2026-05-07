package ru.tbank.practicum.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tbank.practicum.models.enums.SchedulesAction;

@Data
@AllArgsConstructor
public class ScheduleResponseDTO {
    private Long id;
    private Long deviceId;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime executionTime;

    private SchedulesAction action;
    private Double targetValue;
    private boolean active;
}
