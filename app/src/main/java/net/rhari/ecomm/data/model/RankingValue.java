package net.rhari.ecomm.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity(tableName = "ranking_values",
primaryKeys = {"ranking_id", "product_id"})
@Parcel(Parcel.Serialization.BEAN)
public class RankingValue {

    @ColumnInfo(name = "ranking_id")
    private final int rankingId;

    @ColumnInfo(name = "product_id")
    private final int productId;

    @ColumnInfo(name = "value")
    private final int value;

    @ParcelConstructor
    public RankingValue(int rankingId, int productId, int value) {
        this.rankingId = rankingId;
        this.productId = productId;
        this.value = value;
    }

    public int getRankingId() {
        return rankingId;
    }

    public int getProductId() {
        return productId;
    }

    public int getValue() {
        return value;
    }
}
