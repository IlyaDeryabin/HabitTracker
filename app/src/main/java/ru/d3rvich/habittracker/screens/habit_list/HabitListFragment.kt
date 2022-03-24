package ru.d3rvich.habittracker.screens.habit_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.adapters.HabitListPagerAdapter
import ru.d3rvich.habittracker.adapters.PagerItem
import ru.d3rvich.habittracker.databinding.FragmentHabitListBinding
import ru.d3rvich.habittracker.entity.HabitType
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.screens.habit_list.view.FilterFragment
import ru.d3rvich.habittracker.screens.habit_list.view.RemoveHabitDialog
import ru.d3rvich.habittracker.utils.isVisible

class HabitListFragment : Fragment() {
    private val viewModel: HabitListViewModel by viewModels()
    private val binding: FragmentHabitListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val pagerAdapter: HabitListPagerAdapter by lazy {
        HabitListPagerAdapter(onItemClick = { habitId ->
            viewModel.obtainEvent(HabitListEvent.OnHabitSelected(habitId))
        }, onLongClick = {
            RemoveHabitDialog {
                viewModel.obtainEvent(HabitListEvent.OnDeleteHabit(it))
            }.show(childFragmentManager, RemoveHabitDialog.TAG)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val fragment = FilterFragment()
        childFragmentManager.commit {
            replace(R.id.bottom_sheet, fragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addHabitButton.setOnClickListener {
            viewModel.obtainEvent(HabitListEvent.OnAddHabitButtonClicked)
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    binding.addHabitButton.animate().scaleX(0f).scaleY(0f).setDuration(300).start()
                    binding.addHabitButton.isClickable = false
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.addHabitButton.animate().scaleX(1f).scaleY(1f).setDuration(300).start()
                    binding.addHabitButton.isClickable = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            val tabs =
                arrayOf(R.string.pager_title_good, R.string.pager_title_bad).map { getString(it) }
            tab.text = tabs[position]
        }.attach()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progressIndicator.isVisible(state.isLoading)
                        val items = listOf(
                            PagerItem(habits = state.habitList?.filter { it.type == HabitType.Good },
                                targetType = HabitType.Good),
                            PagerItem(habits = state.habitList?.filter { it.type == HabitType.Bad },
                                targetType = HabitType.Bad)
                        )
                        pagerAdapter.submitList(items)
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