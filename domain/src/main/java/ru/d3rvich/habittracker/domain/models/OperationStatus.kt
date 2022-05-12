package ru.d3rvich.habittracker.domain.models

sealed interface OperationStatus {
    object Success : OperationStatus
    object Failure : OperationStatus
}