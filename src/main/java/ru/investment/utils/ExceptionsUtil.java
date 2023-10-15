package ru.investment.utils;

public final class ExceptionsUtil {
    private ExceptionsUtil() {
    }

    public static String getFullExceptionMessage(Exception e) {
        return e.getCause() == null
                ? e.getMessage() : e.getCause().getCause() == null
                ? e.getCause().getMessage() : e.getCause().getCause().getMessage();
    }
}
