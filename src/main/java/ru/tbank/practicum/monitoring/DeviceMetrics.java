package ru.tbank.practicum.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class DeviceMetrics {

    private final MeterRegistry registry;

    public DeviceMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incrementDeviceCommand(String deviceType, String command) {
        Counter.builder("smart_home_device_commands_total")
                .description("Количество команд, отправленных на устройства")
                .tag("device_type", deviceType)
                .tag("command", command)
                .register(registry)
                .increment();
    }
}
