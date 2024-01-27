package ec.constants;

public enum Roles {
    SITE_USER_I(1),
    ADMIN(2);


    private final int value;

    Roles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    }

