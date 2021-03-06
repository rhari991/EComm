package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.Remote;
import net.rhari.ecomm.data.local.CategoryDao;
import net.rhari.ecomm.data.local.ProductDao;
import net.rhari.ecomm.data.local.RankingDao;
import net.rhari.ecomm.data.local.VariantDao;
import net.rhari.ecomm.data.model.ApiResponse;
import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.data.remote.RemoteDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class CategoryRepository {

    private final RemoteDataSource remoteDataSource;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final VariantDao variantDao;
    private final RankingDao rankingDao;

    @Inject
    CategoryRepository(@Remote RemoteDataSource remoteDataSource,
                       @Local CategoryDao categoryDao,
                       @Local ProductDao productDao,
                       @Local VariantDao variantDao,
                       @Local RankingDao rankingDao) {
        this.remoteDataSource = remoteDataSource;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.variantDao = variantDao;
        this.rankingDao = rankingDao;
    }

    public Flowable<List<Category>> getTopLevelCategories() {
        Flowable<List<Category>> remoteDataObservable = remoteDataSource.getInfo()
                        .doOnNext(this::saveData)
                        .flatMap(apiResponse -> getLocalTopLevelCategories())
                        .subscribeOn(Schedulers.io());

        return remoteDataObservable
                .publish(network ->
                        Flowable.merge(network, getLocalTopLevelCategories().takeUntil(network)));
    }

    public Flowable<List<Category>> getChildCategories(int parentCategoryId) {
        return categoryDao.getChildCategories(parentCategoryId)
                .subscribeOn(Schedulers.computation());
    }

    private void saveData(ApiResponse response) {
        categoryDao.insertCategories(response.getCategories());
        productDao.insertProducts(response.getProducts());
        variantDao.insertVariants(response.getVariants());
        rankingDao.insertRankingInfo(response.getRankingInfo());
        rankingDao.insertRankingValues(response.getRankingValues());
    }

    private Flowable<List<Category>> getLocalTopLevelCategories() {
        return categoryDao.getTopLevelCategories()
                .filter(categories -> categories.size() > 0)
                .subscribeOn(Schedulers.computation());
    }
}
