package net.rhari.ecomm.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.RankingValue;
import net.rhari.ecomm.data.model.SortedProduct;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRankingInfo(List<RankingInfo> info);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRankingValues(List<RankingValue> values);

    @Query("SELECT * " +
            "FROM ranking_info")
    Flowable<List<RankingInfo>> getAllRankingInfo();

    @Query("SELECT * " +
            "FROM ranking_values val INNER JOIN products prod " +
            "ON val.product_id = prod.id " +
            "WHERE val.ranking_id = :rankingId AND prod.category_id = :categoryId " +
            "ORDER BY val.value DESC")
    Flowable<List<SortedProduct>> getSortedProducts(int rankingId, int categoryId);
}