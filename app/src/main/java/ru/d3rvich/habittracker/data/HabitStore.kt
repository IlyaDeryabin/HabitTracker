package ru.d3rvich.habittracker.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.d3rvich.habittracker.entity.HabitEntity

object HabitStore {
    private val habits: MutableList<HabitEntity> = mutableListOf()
    private val habitsFlow = MutableStateFlow<List<HabitEntity>>(emptyList())

//    init {
//        runBlocking {
//            launch {
//                for (i in 0 until 10) {
//                    val habit = HabitEntity(
//                        id = UUID.randomUUID().toString(),
//                        title = "Title $i",
//                        description = "Description",
//                        type = if (i % 2 == 0) HabitType.Good else HabitType.Bad,
//                        count = 0,
//                        frequency = 0,
//                        priority = 0,
//                        color = (0xFF00FF00).toInt(),
//                        date = 0L,
//                        doneDates = emptyList()
//                    )
//                    habits.add(habit)
//                }
//                habitsFlow.emit(habits.toList())
//            }
//        }
//    }

    fun getHabits(): Flow<List<HabitEntity>> {
        return habitsFlow.asSharedFlow()
    }

    suspend fun getHabitBy(id: String): HabitEntity {
        delay(1) // Оправдываю suspend
        val habit = habits.find { it.id == id }
        checkNotNull(habit) { "Habit not found" }
        return habit
    }

    suspend fun addHabit(habit: HabitEntity) {
        habits.add(habit)
        habitsFlow.emit(habits.toList())
    }

    suspend fun editHabit(habit: HabitEntity) {
        val toRemove = habits.find { it.id == habit.id }
        checkNotNull(toRemove)
        val indexToRemove = habits.indexOf(toRemove)
        check(indexToRemove > -1) { "Habit not found" }
        habits.removeAt(indexToRemove)
        habits.add(habit)
        habitsFlow.emit(habits.toList())
    }
}