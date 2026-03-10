package com.kmaslowiec.lookgo.common.di.network

import com.kmaslowiec.lookgo.common.utils.ZDITM_BASE_URL
import com.kmaslowiec.lookgo.network.ApiService
import com.kmaslowiec.lookgo.network.cache.ETagCache
import com.kmaslowiec.lookgo.network.interceptor.ETagInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideInterceptor(
        eTagCache: ETagCache,
    ) = ETagInterceptor(
        eTagCache
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(
        eTagInterceptor: ETagInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(eTagInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ZDITM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
