package ru.d3rvich.habittracker.screens.habit_editor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.MainActivity
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.databinding.FragmentHabitEditorBinding
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.entity.HabitType
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorAction
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorEvent
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorViewState
import ru.d3rvich.habittracker.utils.HSVGradient
import ru.d3rvich.habittracker.utils.isVisible
import java.util.*
import javax.inject.Inject

class HabitEditorFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: HabitEditorViewModel.Factory

    private val viewModel: HabitEditorViewModel by lazy {
        val args: HabitEditorFragmentArgs by navArgs()
        viewModelFactory.create(args.habitId)
    }

    private val binding: FragmentHabitEditorBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val gradient = HSVGradient()

    private var habitId: String? = null
    private var doneDates: List<Long>? = null

    override fun onAttach(context: Context) {
        (activity as MainActivity).featureComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.errorView.errorButton.setOnClickListener {
            viewModel.obtainEvent(HabitEditorEvent.OnReloadButtonPressed)
        }
        with(binding.editorView) {
            saveButton.setOnClickListener {
                val habit = collectHabit()
                if (habit == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.obtainEvent(HabitEditorEvent.OnSaveHabitPressed(habit))
                }
            }
            radioGroup.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.good_type_button -> {
                        goodTypeButton.isChecked = true
                        badTypeButton.isChecked = false
                    }
                    R.id.bad_type_button -> {
                        badTypeButton.isChecked = true
                        goodTypeButton.isChecked = false
                    }
                }
            }
            colorView.setBackgroundColor(gradient.getColorAt(seekBar.progress))
        }

        setupPrioritySelector()
        configureColorPicker()

        observeViewModel()
    }

    private fun setupPrioritySelector() {
        val items = arrayOf(R.string.high, R.string.medium, R.string.low).map { getString(it) }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.editorView.habitPriority.adapter = adapter
    }

    private fun configureColorPicker() {
        binding.editorView.seekBar.apply {
            progressDrawable = gradient
            setPadding(0)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    writeColorText(progress)
                    binding.editorView.colorView.setBackgroundColor(gradient.getColorAt(progress))
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        writeColorText(binding.editorView.seekBar.progress)
    }

    private fun writeColorText(colorIndex: Int) {
        val rgb = gradient.getRGBColorAt(colorIndex)
        val hsv = gradient.getHSVColorAt(colorIndex).map { String.format("%.2f", it) }
        binding.editorView.rgbColorValue.text =
            getString(R.string.rgb_text, rgb[0], rgb[1], rgb[2])
        binding.editorView.hsvColorValue.text =
            getString(R.string.hsv_text, hsv[0], hsv[1], hsv[2])
    }

    private fun collectHabit(): HabitEntity? {
        if (!checkFields()) return null
        with(binding.editorView) {
            val habitType = when {
                goodTypeButton.isChecked -> {
                    HabitType.Good
                }
                badTypeButton.isChecked -> {
                    HabitType.Bad
                }
                else -> error("Not found")
            }
            return HabitEntity(
                id = habitId ?: UUID.randomUUID().toString(),
                title = habitTitle.text.toString(),
                description = habitDescription.text.toString(),
                type = habitType,
                count = habitCount.text.toString().toInt(),
                frequency = habitFrequency.text.toString().toInt(),
                priority = habitPriority.selectedItemPosition,
                color = gradient.getColorAt(seekBar.progress),
                date = System.currentTimeMillis(),
                doneDates = doneDates ?: emptyList()
            )
        }
    }

    private fun checkFields(): Boolean {
        with(binding.editorView) {
            if (habitTitle.text.isNullOrEmpty()) return false
            if (habitDescription.text.isNullOrEmpty()) return false
            if (!(goodTypeButton.isChecked || badTypeButton.isChecked)) return false
            if (habitCount.text.isNullOrEmpty()) return false
            if (habitFrequency.text.isNullOrEmpty()) return false
        }
        return true
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { viewState ->
                        cleanUI()
                        when (viewState) {
                            is HabitEditorViewState.Creator -> {
                                showEditor(viewState.isUploading)
                            }
                            is HabitEditorViewState.Editor -> {
                                showEditor(viewState.isUploading)
                                setupHabit(viewState.habit)
                                habitId = viewState.habit.id
                                doneDates = viewState.habit.doneDates
                            }
                            is HabitEditorViewState.Error -> {
                                binding.errorView.root.isVisible(true)
                                binding.errorView.errorText.text = getString(viewState.messageResId)
                            }
                            HabitEditorViewState.Loading -> {
                                binding.loadingView.root.isVisible(true)
                            }
                        }
                    }
                }
                launch {
                    viewModel.uiAction.collect { action ->
                        when (action) {
                            HabitEditorAction.PopBackStack -> {
                                findNavController().navigateUp()
                            }
                            is HabitEditorAction.ShowToast -> {
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

    private fun setupHabit(habit: HabitEntity) {
        with(binding.editorView) {
            habitTitle.setTextIfFieldEmpty(habit.title)
            habitDescription.setTextIfFieldEmpty(habit.description)
            habitCount.setTextIfFieldEmpty(habit.count.toString())
            habitFrequency.setTextIfFieldEmpty(habit.frequency.toString())
            habitPriority.setSelection(habit.priority)
            val hueIndex = gradient.findHuePositionByColor(habit.color)
            seekBar.progress = hueIndex
            when (habit.type) {
                HabitType.Good -> {
                    radioGroup.check(R.id.good_type_button)
                    goodTypeButton.isSelected
                }
                HabitType.Bad -> {
                    radioGroup.check(R.id.bad_type_button)
                    badTypeButton.isSelected
                }
            }
        }
    }

    private fun showEditor(isUploading: Boolean) {
        binding.editorView.root.isVisible(true)
        binding.editorView.saveButton.isEnabled = !isUploading
    }

    private fun cleanUI() {
        binding.errorView.root.isVisible(false)
        binding.editorView.root.isVisible(false)
        binding.loadingView.root.isVisible(false)
    }

    private fun TextInputEditText.setTextIfFieldEmpty(text: String) {
        if (this.text.toString().isEmpty()) {
            setText(text)
        }
    }
}