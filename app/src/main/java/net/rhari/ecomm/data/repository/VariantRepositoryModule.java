package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.local.ECommDatabase;
import net.rhari.ecomm.data.local.VariantDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VariantRepositoryModule {

    @Singleton
    @Provides
    @Local
    VariantDao provideVariantDao(@Local ECommDatabase db) {
        return db.getVariantDao();
    }
}
