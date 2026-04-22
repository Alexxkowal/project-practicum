package ru.tbank.practicum;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.wiremock.spring.EnableWireMock;
import ru.tbank.practicum.kafka.producer.WeatherEventProducer;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@EnableWireMock
@ActiveProfiles("test")
public abstract class BaseIT {

    @MockitoBean
    protected WeatherEventProducer weatherEventProducer;

    @ServiceConnection
    protected static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    static {
        postgres.start();
    }
}
