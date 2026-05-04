package ru.tbank.practicum.services;

import java.util.List;
import ru.tbank.practicum.controllers.dto.CreateDeviceRequestDTO;
import ru.tbank.practicum.controllers.dto.DeviceDetailResponseDTO;
import ru.tbank.practicum.controllers.dto.DevicePatchDTO;
import ru.tbank.practicum.controllers.dto.DeviceSummaryResponseDTO;

public interface DeviceService {

    DeviceDetailResponseDTO create(CreateDeviceRequestDTO dto);

    List<DeviceSummaryResponseDTO> list();

    DeviceDetailResponseDTO getById(Long id);

    DeviceDetailResponseDTO patch(Long id, DevicePatchDTO dto);

    void delete(Long id);
}
