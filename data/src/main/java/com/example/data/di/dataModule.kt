package com.example.data.di

import android.app.Application
import androidx.room.Room
import com.example.data.BuildConfig.BASE_URL
import com.example.data.local.AppDatabase
import com.example.data.local.CartDao
import com.example.data.local.ProductsDao
import com.example.data.remote.api_service.MainApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.data.remote.repository.MainRepositoryImpl
import com.example.data.remote.repository.CartRepositoryImpl
import com.example.domain.repository.MainRepository
import com.example.domain.repository.CartRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf

val dataModule = module {
    singleOf(::provideRetrofit)
    singleOf(::provideOkHttpClient)
    factoryOf(::provideProductApi)
    factoryOf(::MainRepositoryImpl) {
        bind<MainRepository>()
    }
    factoryOf(::CartRepositoryImpl) {
        bind<CartRepository>()
    }
    singleOf(::provideDataBase)
    singleOf(::provideProductsDao)
    singleOf(::provideCartDao)
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
}

fun provideProductApi(retrofit: Retrofit): MainApiService {
    return retrofit.create(MainApiService::class.java)
}

fun provideDataBase(application: Application): AppDatabase =
    Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_db"
    ).
    fallbackToDestructiveMigration().build()

fun provideProductsDao(postDataBase: AppDatabase): ProductsDao = postDataBase.productsDao()

fun provideCartDao(postDataBase: AppDatabase): CartDao = postDataBase.cartDao()