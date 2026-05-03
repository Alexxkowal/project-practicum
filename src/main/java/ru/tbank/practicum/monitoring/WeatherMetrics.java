package ru.tbank.practicum.monitoring;

import com.google.common.util.concurrent.AtomicDouble;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class WeatherMetrics {

    private final Counter weatherEventsCounter;
    private final AtomicDouble currentTemperature;

    public WeatherMetrics(MeterRegistry registry) {
        this.weatherEventsCounter = Counter.builder("smart_home_weather_events_total")
                .description("Общее количество обработанных событий погоды")
                .tag("type", "kafka_consumer")
                .register(registry);
        this.currentTemperature = new AtomicDouble(0);
        Gauge.builder("smart_home_current_temperature", currentTemperature, AtomicDouble::get)
                .description("Последняя полученная температура")
                .tag("sensor", "external_api")
                .register(registry);
    }

    public void incrementEvents() {
        weatherEventsCounter.increment();
    }

    public void updateTemperature(double temp) {
        currentTemperature.set(temp);
    }
}
