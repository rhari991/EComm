package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.local.ProductDao;
import net.rhari.ecomm.data.model.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class ProductRepository {

    private final ProductDao dao;

    @Inject
    ProductRepository(@Local ProductDao dao) {
        this.dao = dao;
    }

    public Flowable<List<Product>> getProducts(int categoryId) {
        return dao.getProducts(categoryId)
                .filter(products -> products.size() > 0)
                .subscribeOn(Schedulers.computation());
    }
}
