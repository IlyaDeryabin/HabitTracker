package ru.d3rvich.habittracker.data.remote

import retrofit2.http.*
import ru.d3rvich.habittracker.data.dto.HabitDoneDto
import ru.d3rvich.habittracker.data.dto.HabitDto
import ru.d3rvich.habittracker.data.dto.HabitUidDto
import ru.d3rvich.habittracker.data.dto.NewHabitDto
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult

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