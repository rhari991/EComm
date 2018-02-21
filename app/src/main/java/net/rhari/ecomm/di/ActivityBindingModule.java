package net.rhari.ecomm.di;

import net.rhari.ecomm.categories.CategoriesActivity;
import net.rhari.ecomm.categories.CategoriesModule;
import net.rhari.ecomm.products.ProductsActivity;
import net.rhari.ecomm.products.ProductsModule;
import net.rhari.ecomm.variants.VariantsActivity;
import net.rhari.ecomm.variants.VariantsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CategoriesModule.class})
    abstract CategoriesActivity categoriesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ProductsModule.class})
    abstract ProductsActivity productsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {VariantsModule.class})
    abstract VariantsActivity variantsActivity();
}