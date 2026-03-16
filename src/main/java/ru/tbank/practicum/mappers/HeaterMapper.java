package ru.tbank.practicum.mappers;


import org.springframework.stereotype.Component;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;
import ru.tbank.practicum.controllers.dto.HeaterResponseDTO;
import ru.tbank.practicum.models.Heater;

@Component
public class HeaterMapper {
    public HeaterResponseDTO toResponse(Heater heater){
        return new HeaterResponseDTO(
                heater.getId(),
                heater.getCurrentTemperature(),
                heater.getTargetTemperature(),
                heater.isWorking()
        );
    }

    public Heater toEntity(HeaterRequestDTO dto){
        Heater heater = new Heater();
        heater.setTargetTemperature(dto.getTargetTemperature());
        heater.setWorking(dto.getIsWorking());
        return heater;
    }
}
