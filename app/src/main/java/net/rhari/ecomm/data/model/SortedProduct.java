package net.rhari.ecomm.data.model;

import android.arch.persistence.room.Embedded;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class SortedProduct {

    @Embedded
    final Product product;

    @Embedded
    final RankingValue rankingValue;

    @ParcelConstructor
    public SortedProduct(Product product, RankingValue rankingValue) {
        this.product = product;
        this.rankingValue = rankingValue;
    }

    public Product getProduct() {
        return product;
    }

    public RankingValue getRankingValue() {
        return rankingValue;
    }
}
