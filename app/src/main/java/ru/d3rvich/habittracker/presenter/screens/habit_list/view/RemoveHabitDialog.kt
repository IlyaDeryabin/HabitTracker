package ru.d3rvich.habittracker.presenter.screens.habit_list.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.d3rvich.habittracker.R

class RemoveHabitDialog(private val onPositive: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.confirm) { _, _ ->
                onPositive()
            }.setMessage(R.string.remove_message).setTitle(R.string.delete_habit).create()

    companion object {
        const val TAG = "DELETE_DIALOG"
    }
}