package ru.d3rvich.habittracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.adapters.HabitListAdapter
import ru.d3rvich.habittracker.databinding.ActivityMainBinding
import ru.d3rvich.habittracker.screens.habit_editor.HabitEditorActivity
import ru.d3rvich.habittracker.screens.habit_editor.HabitEditorViewModel
import ru.d3rvich.habittracker.screens.habit_list.HabitListViewModel
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.utils.isVisible

class MainActivity : AppCompatActivity() {
    private val viewModel: HabitListViewModel by viewModels()
    private val binding: ActivityMainBinding by viewBinding()

    private lateinit var habitListAdapter: HabitListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habitListAdapter = HabitListAdapter { habitId ->
            viewModel.obtainEvent(HabitListEvent.OnHabitSelected(id = habitId))
        }
        binding.habitList.adapter = habitListAdapter
        binding.addHabitButton.setOnClickListener {
            viewModel.obtainEvent(HabitListEvent.OnAddHabitButtonClicked)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progressIndicator.isVisible(state.isLoading)
                        binding.habitList.isVisible(state.habitList != null && state.habitList.isNotEmpty())
                        if (state.habitList != null) {
                            binding.emptyListMessage.isVisible(state.habitList.isEmpty())
                            habitListAdapter.submitList(state.habitList)
                        }
                    }
                }
                launch {
                    viewModel.uiAction.collect { action ->
                        when (action) {
                            HabitListAction.NavigateToHabitCreator -> {
                                val intent = Intent(
                                    this@MainActivity,
                                    HabitEditorActivity::class.java
                                )
                                startActivity(intent)
                            }
                            is HabitListAction.NavigateToHabitEditor -> {
                                val intent = Intent(
                                    this@MainActivity,
                                    HabitEditorActivity::class.java
                                ).apply {
                                    putExtra(HabitEditorViewModel.HABIT_ID_KEY, action.habitId)
                                }
                                startActivity(intent)
                            }
                            is HabitListAction.ShowToast -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(action.massageResId),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}