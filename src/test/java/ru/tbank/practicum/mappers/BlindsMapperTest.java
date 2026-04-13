package ru.tbank.practicum.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.tbank.practicum.controllers.dto.BlindsResponseDTO;
import ru.tbank.practicum.models.Blinds;

class BlindsMapperTest {

    private final BlindsMapper mapper = Mappers.getMapper(BlindsMapper.class);

    @Test
    void shouldMapAllFieldsToResponse() {
        LocalTime open = LocalTime.of(7, 0);
        LocalTime close = LocalTime.of(22, 0);
        Blinds blinds = new Blinds();
        blinds.setId(10L);
        blinds.setCurrentPosition(45);
        blinds.setTargetPosition(100);
        blinds.setOpenTime(open);
        blinds.setCloseTime(close);
        BlindsResponseDTO response = mapper.toResponse(blinds);
        assertAll(
                "Проверка всех полей маппера",
                () -> assertNotNull(response),
                () -> assertEquals(10L, response.getId()),
                () -> assertEquals(45, response.getPosition()),
                () -> assertEquals(open, response.getOpenTime()),
                () -> assertEquals(close, response.getCloseTime()));
    }

    @Test
    void shouldReturnNullWhenSourceIsNull() {
        assertNull(mapper.toResponse(null));
    }
}
