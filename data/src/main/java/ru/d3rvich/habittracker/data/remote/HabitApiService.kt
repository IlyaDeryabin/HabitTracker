package ru.d3rvich.habittracker.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.d3rvich.habittracker.data.dto.HabitDoneDto
import ru.d3rvich.habittracker.data.dto.HabitDto
import ru.d3rvich.habittracker.data.dto.HabitUidDto
import ru.d3rvich.habittracker.data.dto.NewHabitDto
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import ru.d3rvich.habittracker.data.remote.result_adapter.ResultAdapterFactory

private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"

interface HabitApiService {

    @PUT("habit")
    suspend fun editHabit(@Body habit: HabitDto): RetrofitResult<HabitUidDto>

    @PUT("habit")
    suspend fun createHabit(@Body habit: NewHabitDto): RetrofitResult<HabitUidDto>

    @GET("habit")
    suspend fun getHabits(): RetrofitResult<List<HabitDto>>

    @POST("habit_done")
    suspend fun habitDone(@Body habitDone: HabitDoneDto): RetrofitResult<Unit>

    @HTTP(path = "habit", method = "DELETE", hasBody = true)
    suspend fun removeHabit(@Body habitUid: HabitUidDto): RetrofitResult<Unit>
}

fun HabitApiService(apiKey: String): HabitApiService {
    val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val authorizationInterceptor = AuthorizationInterceptor(apiKey = apiKey)
    val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
        addInterceptor(authorizationInterceptor)
    }.build()
    val retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(httpClient)
        addCallAdapterFactory(ResultAdapterFactory())
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    return retrofit.create(HabitApiService::class.java)
}