package ru.tbank.practicum.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;
import ru.tbank.practicum.controllers.dto.HeaterResponseDTO;
import ru.tbank.practicum.models.Heater;

class HeaterMapperTest {

    private final HeaterMapper mapper = Mappers.getMapper(HeaterMapper.class);

    @Test
    public void shouldMapEntityToResponse() {
        Heater heater = new Heater();
        heater.setId(5L);
        heater.setCurrentTemp(22.5);
        heater.setTargetTemp(24.0);
        heater.setWorking(true);
        HeaterResponseDTO response = mapper.toResponse(heater);
        assertAll(
                "Проверка полей ответа",
                () -> assertEquals(5L, response.getId()),
                () -> assertEquals(22.5, response.getCurrentTemp()),
                () -> assertEquals(24.0, response.getTargetTemp()),
                () -> assertTrue(response.isWorking(), "Статус 'работает' должен быть true"));
    }

    @Test
    public void shouldMapRequestToEntity() {
        HeaterRequestDTO request = new HeaterRequestDTO();
        request.setTargetTemp(21.0);
        request.setIsWorking(false);
        Heater heater = mapper.toEntity(request);
        assertAll(
                "Проверка создания сущности",
                () -> assertNotNull(heater),
                () -> assertEquals(21.0, heater.getTargetTemp()),
                () -> assertFalse(heater.isWorking()),
                () -> assertNull(heater.getId(), "ID должен быть null при маппинге из RequestDTO"));
    }
}
