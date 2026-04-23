package ru.tbank.practicum.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.kafka.dto.DeviceCommand;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandProducer {

    @Value("${spring.kafka.topic.actuator-commands}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCommand(DeviceCommand command) {
        log.info("Отправка комманды в топик {} : {}", topic, command);

        String key = String.valueOf(command.deviceId());
        kafkaTemplate.send(topic, key, command);
    }
}
