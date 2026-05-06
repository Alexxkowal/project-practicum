package ru.tbank.practicum.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.tbank.practicum.BaseIT;
import ru.tbank.practicum.repositories.BlindsRepository;
import ru.tbank.practicum.repositories.DeviceRepository;
import ru.tbank.practicum.repositories.HeaterRepository;
import ru.tbank.practicum.repositories.ScheduleRepository;

class DeviceControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private HeaterRepository heaterRepository;

    @Autowired
    private BlindsRepository blindsRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    void clean() {
        scheduleRepository.deleteAll();
        heaterRepository.deleteAll();
        blindsRepository.deleteAll();
        deviceRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /devices: создаёт обогреватель с записью в heaters")
    void createHeater() throws Exception {
        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Тест\",\"type\":\"HEATER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("HEATER"))
                .andExpect(jsonPath("$.heater.targetTemp").value(20.0));
        assertEquals(1, deviceRepository.count());
        assertEquals(1, heaterRepository.count());
    }

    @Test
    @DisplayName("DELETE /devices/{id}: каскадно удаляет дочерние сущности")
    void deleteDevice_cascades() throws Exception {
        String json = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Жалюзи\",\"type\":\"BLINDS\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long id = objectMapper.readTree(json).get("id").asLong();

        mockMvc.perform(delete("/devices/" + id)).andExpect(status().isNoContent());

        assertTrue(deviceRepository.findById(id).isEmpty());
        assertTrue(blindsRepository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("PATCH /devices/{id}: включает авто-режим")
    void patchAutoMode() throws Exception {
        String json = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"H\",\"type\":\"HEATER\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long id = objectMapper.readTree(json).get("id").asLong();

        mockMvc.perform(patch("/devices/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"autoMode\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autoMode").value(true));
    }
}
