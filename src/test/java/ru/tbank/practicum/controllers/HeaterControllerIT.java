package ru.tbank.practicum.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.tbank.practicum.BaseIT;
import ru.tbank.practicum.models.Device;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.models.enums.DeviceStatus;
import ru.tbank.practicum.models.enums.DeviceType;
import ru.tbank.practicum.repositories.DeviceRepository;
import ru.tbank.practicum.repositories.HeaterRepository;

class HeaterControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HeaterRepository heaterRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() {
        heaterRepository.deleteAll();
    }

    @Test
    @DisplayName("PATCH /heaters/{id}: Успешное обновление целевой температуры в БД")
    public void updateTemperature_ShouldUpdateValueInDatabase() throws Exception {
        Device device = new Device();
        device.setName("Умный обогреватель");
        device.setType(DeviceType.HEATER);
        device.setStatus(DeviceStatus.ONLINE);
        device.setAutoMode(false);
        device.setEnabled(true);
        device = deviceRepository.save(device);
        Heater heater = new Heater();
        heater.setDevice(device);
        heater.setCurrentTemp(20.0);
        heater.setTargetTemp(22.0);
        heater.setWorking(true);
        Heater saved = heaterRepository.save(heater);
        mockMvc.perform(patch("/heaters/" + saved.getId() + "/temperature")
                        .contentType("application/json")
                        .content("{\"targetTemp\": 25.5}"))
                .andExpect(status().isOk());
        Heater updated = heaterRepository.findById(saved.getId()).orElseThrow();
        assertEquals(25.5, updated.getTargetTemp());
    }
}
