package ru.tbank.practicum.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.kafka.dto.WeatherEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherEventProducer {

    @Value("${spring.kafka.topic.weather}")
    private String weatherTopic;
    private final KafkaTemplate<String, WeatherEvent> kafkaTemplate;

    public void send(WeatherEvent evt){
        log.info("Send event to kafka {}", evt);
        kafkaTemplate.send(weatherTopic, evt.eventId(), evt);
    }
}
