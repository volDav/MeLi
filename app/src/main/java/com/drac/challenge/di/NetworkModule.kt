package com.drac.challenge.di

import com.drac.challenge.BuildConfig
import com.drac.challenge.network.MeliApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

//@Module
//@InstallIn(ViewModelComponent::class)
object NetworkModule {


    //@Singleton
    //@Provides
    fun provideRetrofit() : Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(httpLoggingInterceptor)
        }

        return Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //@Singleton
    //@Provides
    fun provideApiInterface(retrofit: Retrofit) : MeliApi {
        return retrofit.create(MeliApi::class.java)
    }
}