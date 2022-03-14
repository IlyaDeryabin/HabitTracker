package ru.d3rvich.habittracker.screens.habit_list.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.d3rvich.habittracker.adapters.HabitListAdapter
import ru.d3rvich.habittracker.callbacks.HabitListCallback
import ru.d3rvich.habittracker.databinding.TypedHabitListFragmentBinding
import ru.d3rvich.habittracker.entity.HabitEntity
import ru.d3rvich.habittracker.utils.isVisible

class TypedHabitListFragment : Fragment() {
    private val binding: TypedHabitListFragmentBinding
            by viewBinding(createMethod = CreateMethod.INFLATE)

    private lateinit var adapter: HabitListAdapter

    private var callback: HabitListCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = context as HabitListCallback?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = HabitListAdapter { habitId ->
            callback?.onHabitClicked(habitId)
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
        binding.habitList.adapter = adapter
        arguments?.let { bundle ->
            val json = bundle.getString(HABIT_LIST_KEY)!!
            val habits = Json.decodeFromString<List<HabitEntity>>(json)
            binding.emptyListMessage.isVisible(habits.isEmpty())
            binding.habitList.isVisible(habits.isNotEmpty())
            if (habits.isNotEmpty()) {
                adapter.submitList(habits)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        private const val HABIT_LIST_KEY = "habit_list"

        fun newInstance(habits: List<HabitEntity>): TypedHabitListFragment {
            val json = Json.encodeToString(habits)
            val bundle = Bundle()
            bundle.putString(HABIT_LIST_KEY, json)
            val fragment = TypedHabitListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}