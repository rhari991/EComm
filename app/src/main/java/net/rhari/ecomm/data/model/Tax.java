package net.rhari.ecomm.data.model;

public class Tax {

    private final String name;

    private final int value;

    Tax(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
