package ru.tbank.practicum.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tbank.practicum.exception.BlindsNotFoundException;
import ru.tbank.practicum.mappers.BlindsMapper;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.services.BlindsService;

@WebMvcTest({BlindsController.class, BlindsMapper.class})
class BlindsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlindsService blindsService;

    @Test
    public void updatePosition() throws Exception {
        Long deviceId = 1L;
        Integer newPosition = 20;

        Blinds mockBlinds = new Blinds();
        mockBlinds.setId(deviceId);
        mockBlinds.setCurrentPosition(10);
        mockBlinds.setTargetPosition(20);
        mockBlinds.setOpenTime(LocalTime.of(13, 0));
        mockBlinds.setCloseTime(LocalTime.of(14, 30));

        when(blindsService.updatePosition(deviceId, newPosition)).thenReturn(mockBlinds);

        mockMvc.perform(patch("/blinds/{id}/position", deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": 20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceId))
                .andExpect(jsonPath("$.position").value(10))
                .andExpect(jsonPath("$.openTime").value("13:00"))
                .andExpect(jsonPath("$.closeTime").value("14:30"));
    }

    @Test
    public void updateSchedule() throws Exception {
        Long deviceId = 1L;
        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(22, 0);
        Blinds mockBlinds = new Blinds();
        mockBlinds.setId(deviceId);
        mockBlinds.setOpenTime(open);
        mockBlinds.setCloseTime(close);
        mockBlinds.setCurrentPosition(10);
        mockBlinds.setTargetPosition(10);
        when(blindsService.updateSchedule(deviceId, open, close)).thenReturn(mockBlinds);
        mockMvc.perform(patch("/blinds/{id}/schedule", deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "openTime": "08:00",
                                        "closeTime": "22:00"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceId))
                .andExpect(jsonPath("$.openTime").value("08:00"))
                .andExpect(jsonPath("$.closeTime").value("22:00"));
    }

    @Test
    public void updatePosition_ShouldReturn404_WhenBlindsNotFound() throws Exception {
        Long notExistentId = 999L;
        when(blindsService.updatePosition(eq(notExistentId), anyInt()))
                .thenThrow(new BlindsNotFoundException(notExistentId));

        mockMvc.perform(patch("/blinds/{id}/position", notExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": 50}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePosition_ShouldReturn400_WhenPositionIncorrect() throws Exception {
        mockMvc.perform(patch("/blinds/1/position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"position\": -1}"))
                .andExpect(status().isBadRequest());
    }
}
