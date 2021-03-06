package net.rhari.ecomm.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.data.model.Product;
import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.RankingValue;
import net.rhari.ecomm.data.model.Variant;

@Database(entities = {Category.class, Product.class, Variant.class, RankingInfo.class,
        RankingValue.class}, version = 1)
public abstract class ECommDatabase extends RoomDatabase {

    public static final String NAME = "ecomm";

    public abstract CategoryDao getCategoryDao();

    public abstract ProductDao getProductDao();

    public abstract VariantDao getVariantDao();

    public abstract RankingDao getRankingDao();
}
