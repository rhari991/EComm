package net.rhari.ecomm.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity(tableName = "variants")
@Parcel(Parcel.Serialization.BEAN)
public class Variant {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;

    @ColumnInfo(name = "product_id")
    private final int productId;

    @ColumnInfo(name = "color")
    private final String color;

    @ColumnInfo(name = "size")
    private final Integer size;

    @ColumnInfo(name = "price")
    private final int price;

    @ParcelConstructor
    public Variant(int id, int productId, String color, Integer size, int price) {
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

    public Integer getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return color;
    }
}
