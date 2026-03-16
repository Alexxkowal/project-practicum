package ru.tbank.practicum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blinds {
    private Long id;
    private Integer position;
    private LocalTime openTime;
    private LocalTime closeTime;
}
