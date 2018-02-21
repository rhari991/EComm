package net.rhari.ecomm.variants;

import net.rhari.ecomm.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VariantsModule {

    @ActivityScoped
    @Binds
    abstract VariantsContract.Presenter variantsPresenter(VariantsPresenter presenter);
}