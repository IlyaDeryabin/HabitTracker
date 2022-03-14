package ru.d3rvich.habittracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.d3rvich.habittracker.callbacks.HabitListCallback
import ru.d3rvich.habittracker.screens.habit_list.HabitListFragmentDirections

class MainActivity : AppCompatActivity(), HabitListCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onHabitClicked(habitId: String) {
        val action = HabitListFragmentDirections.actionHabitListFragmentToHabitEditorFragment(habitId)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}