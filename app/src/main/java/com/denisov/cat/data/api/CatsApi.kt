package com.denisov.cat.data.api

import com.denisov.cat.di.scope.PerApplication
import com.denisov.cat.data.entity.CatEntity
import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

@PerApplication
class CatsApi @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {

    private val api by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CatsApiInternal::class.java)
    }

    fun getCats(page: Int): Single<List<CatEntity>> =
        api
            .getCats(page = page)
            .onErrorResumeNext { Single.error(ApiException.Connection()) }

    private interface CatsApiInternal {

        @GET("/v1/images/search")
        fun getCats(
            @Query("limit") limit: Int = 30,
            @Query("page") page: Int = 0
        ): Single<List<CatEntity>>
    }

    private companion object {
        private const val BASE_URL = "https://api.thecatapi.com"
    }
}