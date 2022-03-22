package ru.d3rvich.habittracker.screens.habit_list.model

data class FilterConfig(val filterText: String, val sortStrategy: Int) {
    companion object {
        val Empty = FilterConfig(filterText = "", sortStrategy = 0)
    }
}