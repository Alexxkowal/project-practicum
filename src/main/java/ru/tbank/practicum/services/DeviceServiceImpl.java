package ru.tbank.practicum.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.controllers.dto.BlindsResponseDTO;
import ru.tbank.practicum.controllers.dto.CreateDeviceRequestDTO;
import ru.tbank.practicum.controllers.dto.DeviceDetailResponseDTO;
import ru.tbank.practicum.controllers.dto.DevicePatchDTO;
import ru.tbank.practicum.controllers.dto.DeviceSummaryResponseDTO;
import ru.tbank.practicum.controllers.dto.HeaterResponseDTO;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.mappers.BlindsMapper;
import ru.tbank.practicum.mappers.HeaterMapper;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.models.Device;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.models.enums.DeviceStatus;
import ru.tbank.practicum.models.enums.DeviceType;
import ru.tbank.practicum.repositories.BlindsRepository;
import ru.tbank.practicum.repositories.DeviceRepository;
import ru.tbank.practicum.repositories.HeaterRepository;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final HeaterRepository heaterRepository;
    private final BlindsRepository blindsRepository;
    private final HeaterMapper heaterMapper;
    private final BlindsMapper blindsMapper;

    @Override
    @Transactional
    public DeviceDetailResponseDTO create(CreateDeviceRequestDTO dto) {
        Device device = new Device();
        device.setName(dto.getName().trim());
        device.setType(dto.getType());
        device.setStatus(DeviceStatus.ONLINE);
        device.setAutoMode(false);
        device.setEnabled(true);
        device = deviceRepository.save(device);

        switch (dto.getType()) {
            case HEATER -> {
                Heater heater = new Heater();
                heater.setDevice(device);
                heater.setCurrentTemp(20.0);
                heater.setTargetTemp(20.0);
                heater.setWorking(false);
                heaterRepository.save(heater);
            }
            case BLINDS -> {
                Blinds blinds = new Blinds();
                blinds.setDevice(device);
                blinds.setCurrentPosition(0);
                blinds.setTargetPosition(0);
                blindsRepository.save(blinds);
            }
        }
        return getById(device.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceSummaryResponseDTO> list() {
        return deviceRepository.findAll().stream()
                .map(d -> new DeviceSummaryResponseDTO(
                        d.getId(), d.getName(), d.getType(), d.getStatus(), d.isAutoMode(), d.isEnabled()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceDetailResponseDTO getById(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
        return toDetail(device);
    }

    @Override
    @Transactional
    public DeviceDetailResponseDTO patch(Long id, DevicePatchDTO dto) {
        if (dto.getAutoMode() == null && dto.getEnabled() == null) {
            throw new IllegalArgumentException("Укажите autoMode и/или enabled");
        }
        Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
        if (dto.getAutoMode() != null) {
            device.setAutoMode(dto.getAutoMode());
        }
        if (dto.getEnabled() != null) {
            device.setEnabled(dto.getEnabled());
        }
        deviceRepository.save(device);
        return getById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.existsById(id);
    }

    private DeviceDetailResponseDTO toDetail(Device device) {
        HeaterResponseDTO heaterDto = null;
        BlindsResponseDTO blindsDto = null;
        if (device.getType() == DeviceType.HEATER) {
            Heater heater = heaterRepository
                    .findById(device.getId())
                    .orElseThrow(() -> new DeviceNotFoundException(device.getId()));
            heaterDto = heaterMapper.toResponse(heater);
        } else {
            Blinds blinds = blindsRepository
                    .findById(device.getId())
                    .orElseThrow(() -> new DeviceNotFoundException(device.getId()));
            blindsDto = blindsMapper.toResponse(blinds);
        }
        return new DeviceDetailResponseDTO(
                device.getId(),
                device.getName(),
                device.getType(),
                device.getStatus(),
                device.isAutoMode(),
                device.isEnabled(),
                heaterDto,
                blindsDto);
    }
}
