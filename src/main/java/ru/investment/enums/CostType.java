package ru.investment.enums;

public enum CostType {
    RUB("₽"),
    EUR("€"),
    USD("$"),
    GBP("£"),
    CHF("₣"),
    JPY("¥"),
    INR("₹"),
    UNKNOWN("?");

    final String value;

    CostType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
