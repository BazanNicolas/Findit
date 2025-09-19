package com.products.app.di

import com.products.app.core.NetworkErrorHandler
import com.products.app.data.remote.AuthInterceptor
import com.products.app.data.remote.AutosuggestApi
import com.products.app.data.remote.ProductsApi
import com.products.app.data.repository.AutosuggestRepositoryImpl
import com.products.app.data.repository.ProductsRepositoryImpl
import com.products.app.domain.repository.AutosuggestRepository
import com.products.app.domain.repository.ProductsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides @Singleton @Named("autosuggest")
    fun provideAutosuggestMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient {
        val log = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(log)
            .build()
    }

    @Provides @Singleton @Named("autosuggest")
    fun provideAutosuggestOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides @Singleton @Named("autosuggest")
    fun provideAutosuggestRetrofit(@Named("autosuggest") client: OkHttpClient, @Named("autosuggest") moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://http2.mlstatic.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): ProductsApi =
        retrofit.create(ProductsApi::class.java)

    @Provides @Singleton @Named("autosuggest")
    fun provideAutosuggestApi(@Named("autosuggest") retrofit: Retrofit): AutosuggestApi =
        retrofit.create(AutosuggestApi::class.java)

    @Provides @Singleton
    fun provideRepo(api: ProductsApi, errorHandler: NetworkErrorHandler): ProductsRepository =
        ProductsRepositoryImpl(api, errorHandler)

    @Provides @Singleton
    fun provideAutosuggestRepo(@Named("autosuggest") retrofit: Retrofit, errorHandler: NetworkErrorHandler): AutosuggestRepository =
        AutosuggestRepositoryImpl(retrofit, errorHandler)
}
