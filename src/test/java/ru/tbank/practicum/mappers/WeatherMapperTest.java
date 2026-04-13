package ru.tbank.practicum.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.models.WeatherData;
import ru.tbank.practicum.services.dto.*;

class WeatherMapperTest {
    private final WeatherMapper mapper = Mappers.getMapper(WeatherMapper.class);

    @Test
    void mapToModel_ShouldHandleDeepMappingAndDescription() {
        WeatherResponse response = new WeatherResponse();
        CommonData main = new CommonData();
        main.setTemp(25.5);
        main.setPressure(1012);
        response.setMain(main);
        Coord coord = new Coord();
        coord.setLat(55.0);
        coord.setLon(37.0);
        response.setCoord(coord);
        Weather weatherDesc = new Weather();
        weatherDesc.setDescription("clear sky");
        response.setWeather(List.of(weatherDesc));
        Clouds clouds = new Clouds();
        clouds.setAll(0);
        response.setClouds(clouds);
        Wind wind = new Wind();
        wind.setSpeed(5.5);
        response.setWind(wind);
        WeatherData model = mapper.mapToModel(response);
        assertNotNull(model);
        assertNull(model.getId());
        assertEquals(25.5, model.getTemperature());
        assertEquals("clear sky", model.getDescription());
        assertNotNull(model.getTime());
        assertEquals(55.0, model.getLat());
    }

    @Test
    void shouldMapEntityToDto() {
        WeatherData entity = new WeatherData();
        entity.setId(100L);
        entity.setTemperature(15.5);
        entity.setDescription("overcast clouds");
        entity.setPressure(1005);
        entity.setWindSpeed(3.0);
        WeatherDTO dto = mapper.mapToDto(entity);
        assertAll(
                "Проверка полей WeatherDTO",
                () -> assertNotNull(dto),
                () -> assertEquals(100L, dto.getId(), "ID должен переноситься"),
                () -> assertEquals(15.5, dto.getTemperature(), "Температура не совпадает"),
                () -> assertEquals("overcast clouds", dto.getDescription(), "Описание не совпадает"));
    }
}
