package ru.d3rvich.habittracker.screens.habit_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.adapters.HabitListAdapter
import ru.d3rvich.habittracker.databinding.FragmentHabitListBinding
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.utils.isVisible

class HabitListFragment : Fragment() {
    private val viewModel: HabitListViewModel by viewModels()
    private val binding: FragmentHabitListBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private lateinit var habitListAdapter: HabitListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitListAdapter = HabitListAdapter { habitId ->
            viewModel.obtainEvent(HabitListEvent.OnHabitSelected(id = habitId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.habitList.adapter = habitListAdapter
        binding.addHabitButton.setOnClickListener {
            viewModel.obtainEvent(HabitListEvent.OnAddHabitButtonClicked)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                                val navAction = HabitListFragmentDirections
                                    .actionHabitListFragmentToHabitEditorFragment()
                                findNavController().navigate(navAction)
                            }
                            is HabitListAction.NavigateToHabitEditor -> {
                                val navAction = HabitListFragmentDirections
                                    .actionHabitListFragmentToHabitEditorFragment(action.habitId)
                                findNavController().navigate(navAction)
                            }
                            is HabitListAction.ShowToast -> {
                                Toast.makeText(
                                    requireContext(),
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