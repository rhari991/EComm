package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.local.RankingDao;
import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.SortedProduct;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class RankingRepository {

    private final RankingDao dao;

    @Inject
    RankingRepository(@Local RankingDao dao) {
        this.dao = dao;
    }

    public Flowable<List<RankingInfo>> getAllRankingInfo() {
        return dao.getAllRankingInfo()
                .filter(rankingInfo -> rankingInfo.size() > 0)
                .subscribeOn(Schedulers.computation());
    }

    public Flowable<List<SortedProduct>> getSortedProducts(int rankingId, int categoryId) {
        return dao.getSortedProducts(rankingId, categoryId)
                .filter(products -> products.size() > 0)
                .subscribeOn(Schedulers.computation());
    }
}