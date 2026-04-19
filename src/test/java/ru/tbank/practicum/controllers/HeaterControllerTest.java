package ru.tbank.practicum.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tbank.practicum.exception.HeaterNotFoundException;
import ru.tbank.practicum.mappers.HeaterMapper;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.services.HeaterService;

@WebMvcTest({HeaterController.class, HeaterMapper.class})
class HeaterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HeaterService heaterService;

    @Test
    public void setTemperature() throws Exception {
        Long id = 1L;
        Double newTargetTemperature = 25.0;

        Heater heater = new Heater();
        heater.setId(id);
        heater.setWorking(true);
        heater.setCurrentTemp(20.0);
        heater.setTargetTemp(newTargetTemperature);
        when(heaterService.updateTargetTemperature(id, newTargetTemperature)).thenReturn(heater);

        mockMvc.perform(patch("/heaters/{id}/temperature", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"targetTemp\": 25.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.currentTemp").value(20.0))
                .andExpect(jsonPath("$.targetTemp").value(25.0))
                .andExpect(jsonPath("$.working").value(true));
    }

    @Test
    public void setTemperature_ShouldReturn400_WhenTemperatureIncorrect() throws Exception {
        mockMvc.perform(patch("/heaters/1/temperature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"targetTemp\": 0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void setTemperature_ShouldReturn404_WhenHeaterNotFound() throws Exception {
        Long id = 1L;
        when(heaterService.updateTargetTemperature(id, 25.0)).thenThrow(new HeaterNotFoundException(id));

        mockMvc.perform(patch("/heaters/{id}/temperature", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"targetTemp\": 25}"))
                .andExpect(status().isNotFound());
    }
}
