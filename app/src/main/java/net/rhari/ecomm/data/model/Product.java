package net.rhari.ecomm.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity(tableName = "products")
@Parcel(Parcel.Serialization.BEAN)
public class Product {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "category_id")
    private final int categoryId;

    @Embedded
    private final Tax tax;

    @ParcelConstructor
    public Product(int id, String name, int categoryId, Tax tax) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
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

    public Tax getTax() {
        return tax;
    }
}
