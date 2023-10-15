package ru.investment.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.investment.exceptions.GlobalServiceException;
import ru.investment.exceptions.root.ErrorMessages;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final ObjectMapper mapper;

    public String getUserByToken(String token) throws IOException, GlobalServiceException {
        JsonNode tokenData = null;
        if (token != null && !token.isEmpty() && token.split("\\.").length >= 2) {
            tokenData = mapper.readTree(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            if (tokenData.has("given_name")
                    && !tokenData.get("given_name").isNull()
                    && !tokenData.get("given_name").asText().isEmpty()
            ) {
                return tokenData.get("given_name").asText();
            }

            if (tokenData.has("family_name")
                    && !tokenData.get("family_name").isNull()
                    && !tokenData.get("family_name").asText().isEmpty()
            ) {
                return tokenData.get("family_name").asText();
            }

            if (tokenData.has("email")
                    && !tokenData.get("email").isNull()
                    && !tokenData.get("email").asText().isEmpty()
            ) {
                return tokenData.get("email").asText();
            }
        } else {
            log.warn("У токена не указан пользователь: {}", tokenData);
            throw new GlobalServiceException(ErrorMessages.TOKEN_PARSE_ERROR, tokenData);
        }
        log.warn("Ошибка парсинга токена: {}", tokenData);
        throw new GlobalServiceException(ErrorMessages.TOKEN_PARSE_ERROR, tokenData);
    }
}
