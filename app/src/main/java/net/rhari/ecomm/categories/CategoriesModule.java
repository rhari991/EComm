package net.rhari.ecomm.categories;

import net.rhari.ecomm.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CategoriesModule {

    @ActivityScoped
    @Binds
    abstract CategoriesContract.Presenter categoriesPresenter(CategoriesPresenter presenter);
}