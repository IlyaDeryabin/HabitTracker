package ru.d3rvich.habittracker.data.mappers

import ru.d3rvich.habittracker.data.dto.HabitDto
import ru.d3rvich.habittracker.domain.entity.HabitEntity

fun HabitEntity.toHabitDto(): HabitDto = HabitDto(id = id,
    title = title,
    description = description,
    type = type,
    count = count,
    frequency = frequency,
    priority = priority,
    color = color,
    date = date,
    doneDates = doneDates)

fun HabitDto.toHabitEntity(): HabitEntity = HabitEntity(id = id,
    title = title,
    description = description,
    type = type,
    count = count,
    frequency = frequency,
    priority = priority,
    color = color,
    date = date,
    doneDates = doneDates)