package ru.tbank.practicum.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weather")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double temperature;

    @Column
    private String description;

    @Column
    private Double lat;

    @Column
    private Double lon;

    @Column
    private Integer pressure;

    @Column
    private Integer clouds;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column
    private Integer humidity;
}
