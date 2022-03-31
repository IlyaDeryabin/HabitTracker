package ru.d3rvich.habittracker.data.mappers

import ru.d3rvich.habittracker.data.dto.HabitLocalDto
import ru.d3rvich.habittracker.domain.entity.HabitEntity

fun HabitEntity.toHabitDto(): HabitLocalDto = HabitLocalDto(id = id,
    title = title,
    description = description,
    type = type,
    count = count,
    frequency = frequency,
    priority = priority,
    color = color,
    date = date,
    doneDates = doneDates)

fun HabitLocalDto.toHabitEntity(): HabitEntity = HabitEntity(id = id,
    title = title,
    description = description,
    type = type,
    count = count,
    frequency = frequency,
    priority = priority,
    color = color,
    date = date,
    doneDates = doneDates)