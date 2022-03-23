package ru.d3rvich.habittracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.data.dto.HabitDto

@Dao
interface HabitDao {

    @Query("SELECT * FROM habitdto")
    fun getHabits(): Flow<List<HabitDto>>

    @Query("SELECT * FROM habitdto WHERE id = :id")
    suspend fun getHabitBy(id: String): HabitDto

    @Insert
    suspend fun createHabit(habitDto: HabitDto)

    @Update
    suspend fun editHabit(habitDto: HabitDto)

    @Delete
    suspend fun deleteHabit(habitDto: HabitDto)
}