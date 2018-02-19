package net.rhari;

import net.rhari.ecomm.data.repository.CategoryRepository;
import net.rhari.ecomm.data.repository.ProductRepository;
import net.rhari.ecomm.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class ECommApplication extends DaggerApplication {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    ProductRepository productRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }
}
