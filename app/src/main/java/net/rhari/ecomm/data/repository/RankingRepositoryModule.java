package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.local.ECommDatabase;
import net.rhari.ecomm.data.local.RankingDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RankingRepositoryModule {

    @Singleton
    @Provides
    @Local
    RankingDao provideRankingDao(@Local ECommDatabase db) {
        return db.getRankingDao();
    }
}