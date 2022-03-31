package ru.d3rvich.habittracker.data.dto

import androidx.room.*
import ru.d3rvich.habittracker.domain.entity.HabitType

@Entity(tableName = "habit")
@TypeConverters(HabitConverters::class)
data class HabitLocalDto(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val type: HabitType,
    val count: Int,
    val frequency: Int,
    val priority: Int,
    val color: Int,
    val date: Long,
    @ColumnInfo(name = "done_dates") val doneDates: List<Long>,
)

class HabitConverters {
    @TypeConverter
    fun fromHabitType(habitType: HabitType): Int {
        return habitType.code
    }

    @TypeConverter
    fun toHabitType(code: Int): HabitType {
        return HabitType.values().find { it.code == code }!!
    }

    private val separator = ","

    @TypeConverter
    fun fromDoneDates(doneDates: List<Long>): String {
        return doneDates.joinToString(separator)
    }

    @TypeConverter
    fun toDoneDates(data: String): List<Long> {
        if (data.isEmpty()) {
            return emptyList()
        }
        return data.split(separator).map { it.toLong() }
    }
}