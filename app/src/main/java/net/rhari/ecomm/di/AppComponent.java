package net.rhari.ecomm.di;

import android.app.Application;

import net.rhari.ECommApplication;
import net.rhari.ecomm.data.repository.CategoryRepository;
import net.rhari.ecomm.data.repository.CategoryRepositoryModule;
import net.rhari.ecomm.data.repository.ProductRepository;
import net.rhari.ecomm.data.repository.ProductRepositoryModule;
import net.rhari.ecomm.util.NetworkHelperModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {NetworkHelperModule.class,
        CategoryRepositoryModule.class,
        ProductRepositoryModule.class,
        ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<ECommApplication> {

    CategoryRepository getCategoryRepository();

    ProductRepository getProductRepository();

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}