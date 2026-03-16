package ru.tbank.practicum.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationData {
    private String country;
    private Long sunrise;
    private Long sunset;
}
