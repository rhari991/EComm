package net.rhari.ecomm.data.model;

import android.arch.persistence.room.ColumnInfo;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class Tax {

    @ColumnInfo(name = "tax_name")
    private final String name;

    @ColumnInfo(name = "tax_value")
    private final double value;

    @ParcelConstructor
    public Tax(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
