package ru.d3rvich.habittracker.data.dto

import com.google.gson.annotations.SerializedName

data class NewHabitDto(
    val title: String,
    val description: String,
    val type: Int,
    val count: Int,
    val frequency: Int,
    val priority: Int,
    val color: Int,
    val date: Long,
    @SerializedName("done_dates") val doneDates: List<Long>,
)