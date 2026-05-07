package ru.tbank.practicum.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import lombok.Data;
import ru.tbank.practicum.models.enums.SchedulesAction;

@Data
public class SchedulePatchDTO {

    @JsonFormat(pattern = "H:mm")
    private LocalTime executionTime;

    private SchedulesAction action;
    private Double targetValue;
    private Boolean active;
}
