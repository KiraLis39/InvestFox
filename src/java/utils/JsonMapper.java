package utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonMapper {
    private static ObjectMapper mapper;

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
