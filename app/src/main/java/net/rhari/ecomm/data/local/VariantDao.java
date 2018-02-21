package net.rhari.ecomm.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.rhari.ecomm.data.model.Variant;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface VariantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVariants(List<Variant> variants);

    @Query("SELECT * " +
            "FROM variants " +
            "WHERE product_id = :productId")
    Flowable<List<Variant>> getVariants(int productId);
}