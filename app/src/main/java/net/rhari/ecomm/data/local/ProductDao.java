package net.rhari.ecomm.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.rhari.ecomm.data.model.Product;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<Product> products);

    @Query("SELECT * " +
            "FROM products " +
            "WHERE category_id = :categoryId")
    Flowable<List<Product>> getProducts(int categoryId);
}
