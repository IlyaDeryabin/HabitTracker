package ru.d3rvich.habittracker.screens.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private val binding: FragmentAboutBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioGroup.check(R.id.system_default)
        binding.radioGroup.setOnCheckedChangeListener { _, buttonId ->
            when (buttonId) {
                R.id.light_theme -> {
                    setDefaultNightMode(MODE_NIGHT_NO)
                }
                R.id.dark_theme -> {
                    setDefaultNightMode(MODE_NIGHT_YES)
                }
                R.id.system_default -> {
                    setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }
}