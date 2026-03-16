package ru.tbank.practicum.mappers;

import org.springframework.stereotype.Component;
import ru.tbank.practicum.controllers.dto.BlindsRequestDTO;
import ru.tbank.practicum.controllers.dto.BlindsResponseDTO;
import ru.tbank.practicum.models.Blinds;

@Component
public class BlindsMapper {
    public BlindsResponseDTO toResponse(Blinds blinds){
        return new BlindsResponseDTO(
                blinds.getId(),
                blinds.getPosition(),
                blinds.getOpenTime(),
                blinds.getCloseTime()
        );
    }

    public Blinds toRequest(BlindsRequestDTO dto){
        Blinds blinds = new Blinds();
        blinds.setPosition(dto.getPosition());
        blinds.setOpenTime(dto.getOpenTime());
        blinds.setCloseTime(dto.getCloseTime());
        return blinds;
    }
}
