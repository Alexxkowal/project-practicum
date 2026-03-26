package ru.tbank.practicum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "heaters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Heater {
    @Id
    private Long id;

    @Column(name = "current_temp")
    private Double currentTemp;

    @Column(name = "is_working", nullable = false)
    private boolean isWorking;

    @Column(name = "target_temp")
    private Double targetTemp;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "device_id")
    private Device device;
}
