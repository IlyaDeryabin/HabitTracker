package ru.d3rvich.habittracker.data.dto

import com.google.gson.annotations.SerializedName

data class HabitDoneDto(val date: Long, @SerializedName("habit_uid") val id: String)