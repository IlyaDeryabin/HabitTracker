package ru.d3rvich.habittracker.data.components

import androidx.annotation.RestrictTo
import dagger.Component
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.data.remote.HabitApiService
import ru.d3rvich.habittracker.data.workers.BaseHabitWorker
import kotlin.properties.Delegates

@Component(dependencies = [DataDeps::class])
internal interface DataComponent {

    fun inject(baseHabitWorker: BaseHabitWorker)

    @Component.Builder
    interface Builder {

        fun deps(dataDeps: DataDeps): Builder

        fun build(): DataComponent
    }
}

interface DataDeps {

    val habitApiService: HabitApiService

    val habitDatabase: HabitDatabase
}

interface DataDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: DataDeps

    companion object : DataDepsProvider by DataDepsStore
}

object DataDepsStore : DataDepsProvider {

    override var deps: DataDeps by Delegates.notNull()
}