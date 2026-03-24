package ru.tbank.practicum.models;

import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blinds")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blinds {
    @Id
    private Long id;

    @Column(name = "current_position")
    private Integer currentPosition;

    @Column(name = "target_position")
    private Integer targetPosition;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @OneToOne
    @MapsId
    @JoinColumn(name = "device_id")
    private Device device;
}
