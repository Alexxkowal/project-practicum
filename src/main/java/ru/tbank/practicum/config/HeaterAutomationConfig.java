package ru.tbank.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.automation.heaters")
@Data
public class HeaterAutomationConfig {
    private double targetTemp;
    private double coldThreshold;
    private double stopThreshold;
}
