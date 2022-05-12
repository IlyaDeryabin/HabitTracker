package ru.d3rvich.habittracker.presenter.screens.habit_list.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.d3rvich.habittracker.presenter.MainActivity
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.databinding.FragmentFilterBinding
import ru.d3rvich.habittracker.presenter.screens.habit_list.HabitListViewModel
import ru.d3rvich.habittracker.presenter.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.presenter.screens.habit_list.model.HabitSortingVariants
import ru.d3rvich.habittracker.presenter.screens.habit_list.model.SortDirection
import javax.inject.Inject

class FilterFragment : Fragment() {

    @Inject
    lateinit var viewModel: HabitListViewModel

    private val binding: FragmentFilterBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onAttach(context: Context) {
        (activity as MainActivity).featureComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterInputText.editText?.run {
            setText(viewModel.currentState.filterConfig.filterText)
            doAfterTextChanged { text: Editable? ->
                text?.let {
                    viewModel.obtainEvent(HabitListEvent.OnFilterChange(text.toString()))
                }
            }
        }
        configureSortingSelector(viewModel.currentState.filterConfig.sortingEngine as HabitSortingVariants)
        configureSortingDirectionSelection()
    }

    private fun configureSortingSelector(initialSortingMethod: HabitSortingVariants) {
        val sortingList = arrayOf(R.string.no_sorting to HabitSortingVariants.None,
            R.string.by_title to HabitSortingVariants.ByTitle,
            R.string.by_priority to HabitSortingVariants.ByPriority,
            R.string.by_creation_time to HabitSortingVariants.ByCreatedTime).map { getString(it.first) to it.second }
        val adapter =
            ArrayAdapter(requireContext(), R.layout.list_item, sortingList.map { it.first })
        (binding.sortingSelector.editText as? AutoCompleteTextView)?.run {
            val selectedItemText = sortingList.find { it.second == initialSortingMethod }
            selectedItemText?.let {
                setText(selectedItemText.first)
            }
            setAdapter(adapter)
            setOnItemClickListener { _, _, index, _ ->
                val selectedItem = sortingList[index]
                viewModel.obtainEvent(HabitListEvent.OnSortingMethodChange(selectedItem.second))
            }
        }
    }

    private fun configureSortingDirectionSelection() {
        when (viewModel.currentState.filterConfig.sortDirection) {
            SortDirection.ByAscending -> {
                binding.sortingAscendingButton.isChecked = true
            }
            SortDirection.ByDescending -> {
                binding.sortingDescendingButton.isChecked = true
            }
        }
        binding.sortingDirectionGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            when (checkedId) {
                R.id.sorting_ascending_button -> {
                    if (isChecked) {
                        viewModel.obtainEvent(HabitListEvent.OnSortDirectionChange(SortDirection.ByAscending))
                    }
                }
                R.id.sorting_descending_button -> {
                    if (isChecked) {
                        viewModel.obtainEvent(HabitListEvent.OnSortDirectionChange(SortDirection.ByDescending))
                    }
                }
            }
        }
    }
}