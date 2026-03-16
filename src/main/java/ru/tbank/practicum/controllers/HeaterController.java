package ru.tbank.practicum.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;

@Controller
@RequestMapping("/heaters")
public class BatteryController {
    @PatchMapping("{id}/temperature")
    public HeaterRequestDTO setTemperature(@RequestParam(required = true, name = "temperature") Double temperature){

    }
}
