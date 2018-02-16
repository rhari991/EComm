package net.rhari.ecomm.data.model;

public class Product {

    private final int id;

    private final String name;

    private final int categoryId;

    private final long dateAdded;

    private final Tax tax;

    public Product(int id, String name, int categoryId, long dateAdded, Tax tax) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.dateAdded = dateAdded;
        this.tax = tax;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public Tax getTax() {
        return tax;
    }
}
