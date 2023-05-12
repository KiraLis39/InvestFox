package ru.investment.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@UtilityClass
public class UniversalNumberParser {
    private final String errorMessage = "Не удалось распарсить число: {} ({})";

    public static Float parseFloat(String toParse) {
        try {
            return Float.parseFloat(normalizeNumberString(toParse));
        } catch (NumberFormatException nfe) {
            log.error(errorMessage, toParse, nfe.getMessage());
            throw nfe;
        }
    }

    public static Double parseDouble(String toParse) {
        try {
            return Double.parseDouble(normalizeNumberString(toParse));
        } catch (NumberFormatException nfe) {
            log.error(errorMessage, toParse, nfe.getMessage());
            throw nfe;
        }
    }

    public static Integer parseInt(String toParse) {
        try {
            return Integer.parseInt(normalizeNumberString(toParse));
        } catch (NumberFormatException nfe) {
            log.error(errorMessage, toParse, nfe.getMessage());
            throw nfe;
        }
    }

    public static String normalizeNumberString(String toNormalize) {
        return toNormalize
                .replaceAll("[№₽¥$€৳]", "")
                .replaceAll("\\s", "")
                .replace(" ", "")
                .replace("\u202A", "")
                .replace("\u202C", "")
                .replace("\u00A0", "")
                .replace("Br", "")
                .replace("Ft", "")
                .replace("сом", "")
                .replace("руб.", "")
                .replace(",", ".")
                .replace(".00", ".0")
                .replace("р.", ".")
                .replace("%", "")
                .trim();
    }

    public static BigDecimal parseBigDecimal(String td) {
        return BigDecimal.valueOf(parseDouble(td));
    }
}
