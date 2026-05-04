package ru.tbank.practicum.services;

import java.util.List;
import ru.tbank.practicum.controllers.dto.CreateScheduleRequestDTO;
import ru.tbank.practicum.controllers.dto.SchedulePatchDTO;
import ru.tbank.practicum.controllers.dto.ScheduleResponseDTO;

public interface ScheduleService {

    ScheduleResponseDTO create(Long deviceId, CreateScheduleRequestDTO dto);

    List<ScheduleResponseDTO> listByDevice(Long deviceId);

    ScheduleResponseDTO patch(Long scheduleId, SchedulePatchDTO dto);

    void delete(Long scheduleId);
}
