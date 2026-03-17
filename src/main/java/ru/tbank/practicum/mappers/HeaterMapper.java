package ru.tbank.practicum.mappers;

import org.mapstruct.Mapper;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;
import ru.tbank.practicum.controllers.dto.HeaterResponseDTO;
import ru.tbank.practicum.models.Heater;

@Mapper(componentModel = "spring")
public interface HeaterMapperTest {
    HeaterResponseDTO toResponse(Heater heater);
    Heater toEntity(HeaterRequestDTO dto);

}
