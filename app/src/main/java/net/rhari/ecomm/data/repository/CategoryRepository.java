package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.Remote;
import net.rhari.ecomm.data.local.CategoryDao;
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
    private final CategoryDao dao;

    @Inject
    CategoryRepository(@Remote RemoteDataSource remoteDataSource, @Local CategoryDao dao) {
        this.remoteDataSource = remoteDataSource;
        this.dao = dao;
    }

    public Flowable<List<Category>> getTopLevelCategories() {
        Flowable<List<Category>> remoteDataObservable = remoteDataSource.getInfo()
                        .flatMap(apiResponse -> Flowable.just(apiResponse.getCategories()))
                        .doOnNext(dao::insertCategories)
                        .flatMap(categories -> getLocalTopLevelCategories())
                        .subscribeOn(Schedulers.io());

        return remoteDataObservable
                .publish(network ->
                        Flowable.merge(network, getLocalTopLevelCategories().takeUntil(network)));
    }

    public Flowable<List<Category>> getChildCategories(int parentCategoryId) {
        return dao.getChildCategories(parentCategoryId)
                .filter(categories -> categories.size() > 0)
                .subscribeOn(Schedulers.computation());
    }

    private Flowable<List<Category>> getLocalTopLevelCategories() {
        return dao.getTopLevelCategories()
                .filter(categories -> categories.size() > 0)
                .subscribeOn(Schedulers.computation());
    }
}
