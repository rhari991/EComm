package net.rhari.ecomm.data.model;

public class Variant {

    private final int id;

    private final int productId;

    private final String color;

    private final int size;

    private final int price;

    public Variant(int id, int productId, String color, int size, int price) {
        this.id = id;
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }
}
