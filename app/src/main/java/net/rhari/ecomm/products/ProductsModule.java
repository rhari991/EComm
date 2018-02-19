package net.rhari.ecomm.products;

import net.rhari.ecomm.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProductsModule {

    @ActivityScoped
    @Binds
    abstract ProductsContract.Presenter productsPresenter(ProductsPresenter presenter);
}