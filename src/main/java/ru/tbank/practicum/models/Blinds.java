package ru.tbank.practicum.models;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blinds {
    private Long id;
    private Integer position;
    private LocalTime openTime;
    private LocalTime closeTime;
}
