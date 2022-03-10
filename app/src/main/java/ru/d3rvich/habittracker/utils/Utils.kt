package ru.d3rvich.habittracker.utils

import android.view.View

fun View.isVisible(visible: Boolean) {
    this.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}