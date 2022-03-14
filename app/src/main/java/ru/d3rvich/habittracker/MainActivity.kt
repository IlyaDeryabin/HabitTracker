package ru.d3rvich.habittracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.d3rvich.habittracker.callbacks.HabitListCallback
import ru.d3rvich.habittracker.databinding.ActivityMainBinding
import ru.d3rvich.habittracker.screens.habit_list.HabitListFragmentDirections

class MainActivity : AppCompatActivity(), HabitListCallback {
    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.habit_list_fragment, R.id.about_fragment),
            binding.drawer
        )
        binding.navView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setCheckedItem(R.id.habit_list_fragment)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            menuItem.onNavDestinationSelected(navController)
            binding.drawer.close()
            true
        }
    }

    override fun onHabitClicked(habitId: String) {
        val action =
            HabitListFragmentDirections.actionHabitListFragmentToHabitEditorFragment(habitId)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }
}