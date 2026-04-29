package ru.tbank.practicum.kafka.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.producer.DeviceCommandProducer;
import ru.tbank.practicum.monitoring.WeatherMetrics;
import ru.tbank.practicum.services.strategies.AutomationStrategy;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherEventConsumer {

    private final List<AutomationStrategy> strategyList;
    private final DeviceCommandProducer commandProducer;
    private final WeatherMetrics weatherMetrics;

    @KafkaListener(topics = "${spring.kafka.topic.weather}", groupId = "${spring.get-id:smart-home-group}")
    public void consume(WeatherEvent event) {
        log.info("Событие получено: {}", event);
        weatherMetrics.incrementEvents();
        weatherMetrics.updateTemperature(event.temperature());
        for (AutomationStrategy strategy : strategyList) {
            List<DeviceCommand> commands = strategy.process(event);
            if (!commands.isEmpty()) {
                log.info("Стратегия {} создала {} комманд", strategy.getClass().getSimpleName(), commands.size());
                commands.forEach(commandProducer::sendCommand);
            }
        }
    }
}
