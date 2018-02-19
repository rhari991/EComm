package net.rhari.ecomm.di;

import net.rhari.ecomm.categories.CategoriesActivity;
import net.rhari.ecomm.categories.CategoriesModule;
import net.rhari.ecomm.products.ProductsActivity;
import net.rhari.ecomm.products.ProductsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CategoriesModule.class, ProductsModule.class})
    abstract CategoriesActivity categoriesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ProductsModule.class)
    abstract ProductsActivity productsActivity();
}