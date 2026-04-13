package ru.tbank.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tbank.practicum.controllers.dto.BlindsRequestDTO;
import ru.tbank.practicum.controllers.dto.BlindsResponseDTO;
import ru.tbank.practicum.models.Blinds;

@Mapper(componentModel = "spring")
public interface BlindsMapper {
    @Mapping(target = "position", source = "currentPosition")
    BlindsResponseDTO toResponse(Blinds blinds);

    Blinds toModel(BlindsRequestDTO blindsRequestDTO);
}
