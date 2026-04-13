package ru.tbank.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.controllers.dto.BlindsRequestDTO;
import ru.tbank.practicum.controllers.dto.BlindsResponseDTO;
import ru.tbank.practicum.mappers.BlindsMapper;
import ru.tbank.practicum.services.BlindsService;

@RestController
@RequestMapping("/blinds")
@RequiredArgsConstructor
public class BlindsController {
    private final BlindsService blindsService;
    private final BlindsMapper blindsMapper;

    @PatchMapping("/{id}/position")
    public BlindsResponseDTO updatePosition(@PathVariable Long id, @Valid @RequestBody BlindsRequestDTO dto) {
        return blindsMapper.toResponse(blindsService.updatePosition(id, dto.getPosition()));
    }

    @PatchMapping("/{id}/schedule")
    public BlindsResponseDTO updateSchedule(@PathVariable Long id, @Valid @RequestBody BlindsRequestDTO dto) {
        return blindsMapper.toResponse(blindsService.updateSchedule(id, dto.getOpenTime(), dto.getCloseTime()));
    }
}
