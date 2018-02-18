package net.rhari.ecomm.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.rhari.ecomm.data.model.Category;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(List<Category> categories);

    @Query("SELECT * " +
            "FROM categories c1 " +
            "WHERE c1.id = c1.parent_id")
    Flowable<List<Category>> getTopLevelCategories();

    @Query("SELECT * " +
    "FROM categories c1 " +
    "WHERE c1.parent_id = :parentId AND  c1.id != c1.parent_id")
    Flowable<List<Category>> getChildCategories(int parentId);
}
