package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.local.ECommDatabase;
import net.rhari.ecomm.data.local.ProductDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductRepositoryModule {

    @Singleton
    @Provides
    @Local
    ProductDao provideProductDao(@Local ECommDatabase db) {
        return db.getProductDao();
    }
}