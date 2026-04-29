package ru.tbank.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "automation.app.blinds")
public class BlindsAutomationConfig {
    private int closedPosition = 0;
    private int openedPosition = 100;
    private int sunnyPosition = 20;
    private double highTempThreshold = 25.0;
    private double lowTempThreshold = 5.0;
    private int atmospherePosition = 100;
    private int rainSnowWarmPosition = 100;
    private int rainSnowColdPosition = 50;
}
