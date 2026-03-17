package ru.tbank.practicum.mappers;

import org.mapstruct.Mapper;
import ru.tbank.practicum.controllers.dto.BlindsRequestDTO;
import ru.tbank.practicum.controllers.dto.BlindsResponseDTO;
import ru.tbank.practicum.models.Blinds;

@Mapper(componentModel = "spring")
public interface BlindsMapper {
    BlindsResponseDTO toResponse(Blinds blinds);

    Blinds toModel(BlindsRequestDTO blindsRequestDTO);
}
