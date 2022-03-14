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
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.adapters.HabitListPagerAdapter
import ru.d3rvich.habittracker.databinding.FragmentHabitListBinding
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.utils.isVisible

class HabitListFragment : Fragment() {
    private val viewModel: HabitListViewModel by viewModels()
    private val binding: FragmentHabitListBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addHabitButton.setOnClickListener {
            viewModel.obtainEvent(HabitListEvent.OnAddHabitButtonClicked)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progressIndicator.isVisible(state.isLoading)
                        val pagerAdapter =
                            HabitListPagerAdapter(this@HabitListFragment, state.habitList)
                        binding.viewPager.adapter = pagerAdapter
                        TabLayoutMediator(
                            binding.tabLayout,
                            binding.viewPager
                        ) { tab, position ->
                            when (position) {
                                0 -> {
                                    tab.text = getString(R.string.good)
                                }
                                else -> {
                                    tab.text = getString(R.string.bad)
                                }
                            }
                        }.attach()
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