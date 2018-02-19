package net.rhari.ecomm.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity(tableName = "ranking_info")
@Parcel(Parcel.Serialization.BEAN)
public class RankingInfo {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;

    @ColumnInfo(name = "description")
    private final String description;

    @ColumnInfo(name = "metric_name")
    private final String metricName;

    @ParcelConstructor
    public RankingInfo(int id, String description, String metricName) {
        this.id = id;
        this.description = description;
        this.metricName = metricName;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMetricName() {
        return metricName;
    }

    @Override
    public String toString() {
        return description;
    }
}
