package ru.tbank.practicum.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.services.BlindsService;
import ru.tbank.practicum.services.HeaterService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActuatorCommandConsumer {
    private final HeaterService heaterService;
    private final BlindsService blindsService;

    @KafkaListener(topics = "${spring.kafka.topic.actuator-commands}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(DeviceCommand command) {
        log.info("Получена команда из kafka {}", command);
        try {

            switch (command.deviceType().toUpperCase()) {
                case "HEATER" -> {
                    heaterService.processCommand(command);
                    log.debug("Команда успешно передана в HeaterService");
                }
                case "BLINDS" -> {
                    blindsService.processCommand(command);
                    log.debug("Команда успешно передана в BlindsService");
                }
                default -> log.warn("Неизвестный тип устройства в команде: {}", command.deviceType());
            }
        } catch (Exception e) {
            log.error("Ошибка при выполнении команды для устройства {}: {}", command.deviceId(), e.getMessage());
        }
    }
}
