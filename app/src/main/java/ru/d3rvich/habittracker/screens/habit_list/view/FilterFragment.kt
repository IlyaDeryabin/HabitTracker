package ru.d3rvich.habittracker.screens.habit_list.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.d3rvich.habittracker.databinding.FragmentFilterBinding
import ru.d3rvich.habittracker.screens.habit_list.HabitListViewModel
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent

class FilterFragment : Fragment() {
    private val binding: FragmentFilterBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: HabitListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterInputText.doAfterTextChanged { text: Editable? ->
            text?.let {
                viewModel.obtainEvent(HabitListEvent.OnFilterChange(text.toString()))
            }
        }
    }
}