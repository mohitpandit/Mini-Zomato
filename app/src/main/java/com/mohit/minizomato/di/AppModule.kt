package com.mohit.minizomato.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mohit.minizomato.BuildConfig
import com.mohit.minizomato.data.AppDbHelper
import com.mohit.minizomato.data.retrofit.ApiInterface
import com.mohit.minizomato.data.retrofit.LiveDataCallAdapterFactory
import com.mohit.minizomato.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {


    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideHttpClient(getLoggingInterceptor: HttpLoggingInterceptor?): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(getLoggingInterceptor)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }
        return logging
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(
                LiveDataCallAdapterFactory()
            ).client(okHttpClient).build()
    }

    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideDbHelper(appDatabase: AppDatabase): AppDbHelper {
        return AppDbHelper(appDatabase)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder<AppDatabase>(context, AppDatabase::class.java, "datebaseName")
            .fallbackToDestructiveMigration().build()
    }

}