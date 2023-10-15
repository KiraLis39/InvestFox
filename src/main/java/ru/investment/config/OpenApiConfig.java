package ru.investment.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("InvestFox")
                        .description("Сервис инвестирования KiraLis39")
                        .version("API 0.0.1") // as application:version
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Инвестиции в Excel")
                        .url("https://docs.google.com/spreadsheets/d/1xZJ21Cb16xIykAI5FqC6z-M0WkDN43OXFc5Zu2_FxJs/edit#gid=830301191"));
    }
}
