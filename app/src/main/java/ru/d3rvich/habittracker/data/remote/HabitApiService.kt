package ru.d3rvich.habittracker.data.remote

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import ru.d3rvich.habittracker.data.dto.HabitDoneDto
import ru.d3rvich.habittracker.data.dto.HabitRemoteDto

interface HabitApiService {

    @GET("habit")
    suspend fun getHabits(): List<HabitRemoteDto>

    @POST("habit_done")
    suspend fun habitDone(habitDone: HabitDoneDto): Response<Unit>

    @DELETE("habit")
    suspend fun removeHabit(habitId: String): Response<Unit>
}