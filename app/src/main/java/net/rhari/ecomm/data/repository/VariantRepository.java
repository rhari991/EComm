package net.rhari.ecomm.data.repository;

import net.rhari.ecomm.data.Local;
import net.rhari.ecomm.data.local.VariantDao;
import net.rhari.ecomm.data.model.Variant;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class VariantRepository {

    private final VariantDao dao;

    @Inject
    VariantRepository(@Local VariantDao dao) {
        this.dao = dao;
    }

    public Flowable<List<Variant>> getVariants(int productId) {
        return dao.getVariants(productId)
                .filter(variants -> variants.size() > 0)
                .subscribeOn(Schedulers.computation());
    }
}