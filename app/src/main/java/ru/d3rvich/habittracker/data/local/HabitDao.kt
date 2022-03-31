package ru.d3rvich.habittracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.data.dto.HabitLocalDto

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit")
    fun getHabits(): Flow<List<HabitLocalDto>>

    @Query("SELECT * FROM habit WHERE id = :id")
    suspend fun getHabitBy(id: String): HabitLocalDto

    @Insert
    suspend fun createHabit(habitDto: HabitLocalDto)

    @Update
    suspend fun editHabit(habitDto: HabitLocalDto)

    @Delete
    suspend fun deleteHabit(habitDto: HabitLocalDto)
}