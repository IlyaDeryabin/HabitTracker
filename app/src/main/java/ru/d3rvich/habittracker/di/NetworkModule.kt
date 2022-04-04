package ru.d3rvich.habittracker.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.d3rvich.habittracker.BuildConfig
import ru.d3rvich.habittracker.data.remote.AuthorizationInterceptor
import ru.d3rvich.habittracker.data.remote.HabitApiService
import javax.inject.Singleton

@Module
object NetworkModule {
    private const val BASE_URL = "https://doublet.app/droid/8/api/"

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor {
        return AuthorizationInterceptor(apiKey = BuildConfig.API_KEY)
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authorizationInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(onHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(onHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    @Provides
    @Singleton
    fun provideHabitApiService(retrofit: Retrofit): HabitApiService {
        return retrofit.create(HabitApiService::class.java)
    }
}