package ru.d3rvich.habittracker.data.mappers

import ru.d3rvich.habittracker.data.dto.HabitDto
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.entity.HabitType

fun HabitEntity.toHabitDto(): HabitDto {
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

fun HabitDto.toHabitEntity(): HabitEntity {
    val habitType =
        HabitType.values().find { it.code == type } ?: error("Неизвествый код типа привычки: $type")
    return HabitEntity(id = id,
        title = title,
        description = description,
        type = habitType,
        count = count,
        frequency = frequency,
        priority = priority,
        color = color,
        date = date,
        doneDates = doneDates)
}