package net.rhari.ecomm.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity(tableName = "categories")
@Parcel(Parcel.Serialization.BEAN)
public class Category {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;

    @ColumnInfo(name = "name")
    private final String name;

    // Represents the id of the next higher category. If there are no higher categories, it is
    // equal to the category id
    @ColumnInfo(name = "parent_id")
    private int parentId;

    @ParcelConstructor
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
