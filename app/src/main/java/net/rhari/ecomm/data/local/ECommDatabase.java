package net.rhari.ecomm.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import net.rhari.ecomm.data.model.Category;

@Database(entities = {Category.class}, version = 1)
public abstract class ECommDatabase extends RoomDatabase {

    public static final String NAME = "ecomm";

    public abstract CategoryDao getCategoryDao();
}
