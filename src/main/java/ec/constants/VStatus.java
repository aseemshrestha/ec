package ec.constants;

public enum VStatus {
    NOT_APPLIED("NOT APPLIED"),
    APPLIED("APPLIED"),
    APPROVED("APPROVED"),
    DENIED("DENIED");

    private final String value;

    VStatus(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
