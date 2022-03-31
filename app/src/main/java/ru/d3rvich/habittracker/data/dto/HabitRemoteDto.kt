package ru.d3rvich.habittracker.data.dto

import com.google.gson.annotations.SerializedName
import ru.d3rvich.habittracker.domain.entity.HabitType

data class HabitRemoteDto(
    @SerializedName("uid") val id: String,
    val title: String,
    val description: String,
    val type: HabitType,
    val count: Int,
    val frequency: Int,
    val priority: Int,
    val color: Int,
    val date: Long,
    @SerializedName("done_dates") val doneDates: List<Long>,
)