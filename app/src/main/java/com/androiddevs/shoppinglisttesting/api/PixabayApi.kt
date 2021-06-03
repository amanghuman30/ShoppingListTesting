package com.androiddevs.shoppinglisttesting.api

import com.androiddevs.shoppinglisttesting.BuildConfig
import com.androiddevs.shoppinglisttesting.data.models.PixbayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET("/api/")
    suspend fun searchForImages(
        @Query("q") query : String,
        @Query("key") apiKey : String = BuildConfig.PIXBAY_API_KEY
    ) : Response<PixbayResponse>

}