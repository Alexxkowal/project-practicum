package ru.tbank.practicum.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Data;
import ru.tbank.practicum.models.enums.SchedulesAction;

@Data
public class CreateScheduleRequestDTO {
    @NotNull
    @JsonFormat(pattern = "H:mm")
    private LocalTime executionTime;

    @NotNull
    private SchedulesAction action;

    private Double targetValue;

    private Boolean active;
}
