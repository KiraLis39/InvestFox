package registry;

public enum CostType {
    RUB("₽"),
    EUR("€"),
    USD("$"),
    GBP("?"),
    CHF("?"),
    JPY("?");

    CostType(String value) {
        this.value = value;
    }

    String value;

    public String value() {
        return value;
    }
}
