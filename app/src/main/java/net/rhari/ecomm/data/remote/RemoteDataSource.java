package net.rhari.ecomm.data.remote;

import net.rhari.ecomm.data.model.ApiResponse;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Singleton
public class RemoteDataSource {

    private final ApiService apiService;

    public RemoteDataSource() {
        apiService = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(ApiResponseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public Flowable<ApiResponse> getInfo() {
        return apiService.getInfo();
    }
}
