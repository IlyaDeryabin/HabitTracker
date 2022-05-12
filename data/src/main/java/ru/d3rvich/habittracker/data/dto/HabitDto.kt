package ru.d3rvich.habittracker.data.dto

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "habit")
@TypeConverters(HabitConverters::class)
data class HabitDto(
    @PrimaryKey @SerializedName("uid") val id: String,
    val title: String,
    val description: String,
    val type: Int,
    val count: Int,
    val frequency: Int,
    val priority: Int,
    val color: Int,
    val date: Long,
    @ColumnInfo(name = "done_dates") @SerializedName("done_dates") val doneDates: List<Long>,
)

class HabitConverters {
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