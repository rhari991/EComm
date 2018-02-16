package net.rhari.ecomm.data.model;

public class Category {

    private final int id;

    private final String name;

    // Represents the id of the next higher category. If there are no higher categories, it is
    // equal to the category id
    private int parentId;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        // By default, assume that every category does not have a parent
        this.parentId = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
