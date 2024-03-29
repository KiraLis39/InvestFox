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
            return Integer.parseInt(normalizeNumberString(toParse)
                    .replace(".0", ""));
        } catch (NumberFormatException nfe) {
            log.error(errorMessage, toParse, nfe.getMessage());
            throw nfe;
        }
    }

    public static String normalizeNumberString(String toNormalize) {
        String normalized;
        normalized = toNormalize
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
                .replace("р.", "")
                .replace("%", "")
                .trim();

        if (normalized.contains(".") && normalized.contains(",")) {
            normalized = normalized
                    .replace(".", "")
                    .replace(",", ".");
        } else {
            normalized = normalized
                    .replace(",", ".")
                    .replace(".00", ".0");
        }

        if (normalized.endsWith("K")) {
            normalized = normalized
                    .replace("K", "")
                    .replace(".", "000.");
        }

        return normalized;
    }

    public static BigDecimal parseBigDecimal(String td) {
        return BigDecimal.valueOf(parseDouble(td));
    }
}
