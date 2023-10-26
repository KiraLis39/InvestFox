package ru.investment.utils;

import com.codeborne.selenide.SelenideElement;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Парсит даты публикаций новостей и закупок в разных форматах по тайм-зоне GMT.
 */
@UtilityClass
public class UniversalDateParser {

    public static Instant now() {
        return LocalDateTime.now().toInstant(ZoneOffset.ofHours(0));
        // todo: или всё же лучше "LocalDateTime.now(ZoneId.of("Europe/Moscow")).toInstant(ZoneOffset.UTC)"
        //  уточнить, заменить при необходимости.
    }

    public static Instant parseDateTime(String rawDateStr) throws Exception {
        return toInstant(rawDateStr, DateZone.GMT);
    }

    public static Instant parseDateTimeMoskow(String rawDateStr) throws Exception {
        return toInstant(rawDateStr, DateZone.MOSKOW);
    }

    public static Instant parseDateTimeGMT(String rawDateStr) throws Exception {
        return toInstant(rawDateStr, DateZone.GMT);
    }

    public static Instant parseDateTimeFromSE(SelenideElement dataElement) throws Exception {
        return toInstant(dataElement.text().trim(), DateZone.GMT);
    }

    private static Instant toInstant(String rawDateStr, DateZone zone) throws Exception {
        SimpleDateFormat formatter = null;

        if (rawDateStr.contains(", ")) {
            // Создаем парсер для формата строковых дат вида "20 октября 2003, 17:10:12":
            if (rawDateStr.split(":").length == 2) {
                rawDateStr += ":00";
            }
            formatter = new SimpleDateFormat("dd MMMMM yyyy, HH:mm:ss", new Locale("ru", "RU"));
        } else if (rawDateStr.contains(":")) {
            if (rawDateStr.contains("(МСК") && rawDateStr.split(":").length == 2) {
                // Создаем парсер для формата строковых дат вида "20.06.2003 17:10":
                formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("ru", "RU"));
            } else if (rawDateStr.split(":").length == 3) {
                // Создаем парсер для формата строковых дат вида "20.06.2003 17:10:00":
                formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", new Locale("ru", "RU"));
            } else if (rawDateStr.split(":").length == 2) {
                // Создаем парсер для формата строковых дат вида "17:10":
                return null; // время без даты как парсить в инстант - не ясно пока. Оно применимо к любому дню.
            }
        } else if (rawDateStr.contains("/")) {
            // Создаем парсер для формата строковых дат вида "20/06/2003":
            formatter = new SimpleDateFormat("dd/MM/yyyy", new Locale("ru", "RU"));
        } else {
            // Создаем парсер для формата строковых дат вида "20.06.2003":
            formatter = new SimpleDateFormat("dd.MM.yyyy", new Locale("ru", "RU"));
        }

        try {
            if (formatter != null) {
                formatter.setTimeZone(TimeZone.getTimeZone(zone.value()));
                return formatter.parse(rawDateStr).toInstant();
            }
            return null;
        } catch (ParseException e) {
            throw new Exception("Не удалось распарсить дату с сайта: '%s'".formatted(rawDateStr));
        }
    }

    private enum DateZone {
        MOSKOW("Europe/Moscow"),
        GMT("GMT");

        private final String value;

        DateZone(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
