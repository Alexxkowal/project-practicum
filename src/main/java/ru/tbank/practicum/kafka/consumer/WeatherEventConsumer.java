package ru.tbank.practicum.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.kafka.dto.WeatherEvent;

@Slf4j
@Service
public class WeatherEventConsumer {
    @KafkaListener(
            topics = "${spring.kafka.topic.weather}",
            groupId = "${spring.get-id:smart-home-group}"
    )
    public void consume(WeatherEvent event){
        log.info("Событие получено: {}", event);

    }
}
