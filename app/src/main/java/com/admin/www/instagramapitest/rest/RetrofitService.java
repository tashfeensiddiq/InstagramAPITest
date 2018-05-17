package com.admin.www.instagramapitest.rest;

import com.admin.www.instagramapitest.InstagramResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("v1/tags/{tag_name}/media/recent")
    retrofit2.Call<InstagramResponse> getTagPhotos(@Path("tag_name") String tag_name,
                                         @Query("access_token") String access_token);
}