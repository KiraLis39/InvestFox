package ru.investment.enums;

public enum CostType {
    RUB("₽"),
    EUR("€"),
    USD("$"),
    GBP("£"),
    CHF("₣"),
    JPY("¥"),
    INR("₹"),
    NGN("₦"),
    UNKNOWN("?");

    private final String value;

    CostType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
