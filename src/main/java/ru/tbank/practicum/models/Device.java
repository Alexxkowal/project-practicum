package ru.tbank.practicum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbank.practicum.models.enums.DeviceStatus;
import ru.tbank.practicum.models.enums.DeviceType;

@Entity
@Table(name = "devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;

    @Column(name = "auto_mode", nullable = false)
    private boolean autoMode;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;
}
