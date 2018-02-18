package net.rhari.ecomm.data.remote;

import net.rhari.ecomm.data.model.ApiResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;

interface ApiService {

    String BASE_URL = "https://stark-spire-93433.herokuapp.com/";

    @GET("json")
    Flowable<ApiResponse> getInfo();
}
