package net.rhari.ecomm.data.repository;

import android.app.Application;
import android.arch.persistence.room.Room;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.Remote;
import net.rhari.ecomm.data.local.CategoryDao;
import net.rhari.ecomm.data.local.ECommDatabase;
import net.rhari.ecomm.data.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CategoryRepositoryModule {

    @Singleton
    @Provides
    @Remote
    RemoteDataSource provideRemoteDataSource() {
        return new RemoteDataSource();
    }

    @Singleton
    @Provides
    @Local
    ECommDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), ECommDatabase.class,
                ECommDatabase.NAME).build();
    }

    @Singleton
    @Provides
    @Local
    CategoryDao provideCategoryDao(@Local ECommDatabase db) {
        return db.getCategoryDao();
    }
}