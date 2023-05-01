package ru.investment.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ObjectMapperConfig {
    private static ObjectMapper mapper;

    private ObjectMapperConfig() {
    }

    @Bean
    public static ObjectMapper getMapper() {
        if (mapper == null) {
            final JavaTimeModule timeModule = new JavaTimeModule();
            timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            mapper = new ObjectMapper() {
                {
                    registerModule(timeModule);
                    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                }
            };
        }

        return mapper;
    }
}
