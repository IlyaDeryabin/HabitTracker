package ru.d3rvich.habittracker.data.mappers

import ru.d3rvich.habittracker.data.dto.HabitDto
import ru.d3rvich.habittracker.data.dto.NewHabitDto
import ru.d3rvich.habittracker.domain.entity.NewHabitEntity

fun NewHabitDto.toHabitDto(id: String): HabitDto {
    return HabitDto(id = id,
        title = title,
        description = description,
        type = type,
        count = count,
        frequency = frequency,
        priority = priority,
        color = color,
        date = date,
        doneDates = doneDates)
}

fun NewHabitEntity.toNewHabitRemoteDto(): NewHabitDto {
    return NewHabitDto(
        title = title,
        description = description,
        type = type.code,
        count = count,
        frequency = frequency,
        priority = priority,
        color = color,
        date = date,
        doneDates = doneDates)
}

fun NewHabitEntity.toHabitDto(id: String): HabitDto {
    return HabitDto(id = id,
        title = title,
        description = description,
        type = type.code,
        count = count,
        frequency = frequency,
        priority = priority,
        color = color,
        date = date,
        doneDates = doneDates)
}