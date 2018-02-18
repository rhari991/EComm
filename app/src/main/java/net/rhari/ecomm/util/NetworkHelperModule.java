package net.rhari.ecomm.util;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkHelperModule {

    @Singleton
    @Provides
    NetworkHelper provideNetworkHelper(Context context) {
        return new AndroidNetworkHelper(context);
    }
}
