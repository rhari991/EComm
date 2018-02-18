package net.rhari.ecomm.di;

import net.rhari.ecomm.categories.CategoriesActivity;
import net.rhari.ecomm.categories.CategoriesModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = CategoriesModule.class)
    abstract CategoriesActivity categoriesActivity();
}